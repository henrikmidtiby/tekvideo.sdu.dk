package dk.sdu.tekvideo

import dk.sdu.tekvideo.events.AnswerQuestionEvent
import dk.sdu.tekvideo.events.VisitVideoEvent
import org.apache.http.HttpStatus
import org.hibernate.SessionFactory

import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

import static dk.sdu.tekvideo.ServiceResult.*

import grails.transaction.Transactional

@Transactional
class DashboardService {
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("dd/MM HH:mm")

    def urlMappingService
    CourseManagementService courseManagementService
    SessionFactory sessionFactory

    private Course findCourse(Node node) {
        while (node != null && node.parent != null) {
            node = node.parent
        }
        return (node instanceof Course) ? node : null
    }

    /**
     * Returns a node from its identifier.
     *
     * The identifier is of the form "<type>/<id>". The type represents the type of node, these can be
     * ("course", "subject", or "video"). The identifier corresponds to the database, i.e. Course#id, Subject#id, and
     * Video#id.
     *
     * This method will fail if any of the following conditions are met:
     *
     * <ul>
     *   <li>The identifier is not of the correct format</li>
     *   <li>The identifier is of the correct format, but the identifier doesn't point to a valid entry</li>
     *   <li>The identifier is not a valid number</li>
     * </ul>
     *
     * @param identifier The identifier
     * @return The corresponding node, if successful. Otherwise an appropriate error will be returned
     */
    ServiceResult<Node> nodeFromIdentifier(String identifier) {
        def split = identifier.split("/")
        if (split.length != 2) {
            return fail(message: "Invalid identifier")
        } else {
            try {
                def id = Long.parseLong(split[1])
                Node result = null
                switch (split[0]) {
                    case "course":
                        result = Course.get(id)
                        break
                    case "subject":
                        result = Subject.get(id)
                        break
                    case "video":
                        result = Video.get(id)
                        break
                }
                if (result != null) {
                    return ok(item: result)
                } else {
                    return fail(message: "Node not found!")
                }
            } catch (NumberFormatException ignored) {
                return fail(message: "Invalid ID")
            }
        }
    }

    /**
     * Finds the leaves (i.e. videos) starting at a particular node.
     *
     * This method will fail if the node is of an unknown type, or if the authenticated user doesn't own the course.
     *
     * No ordering of the videos are preserved.
     *
     * @param node The node to start searching at
     * @return A list of videos (unordered).
     */
    ServiceResult<List<Video>> findLeaves(Node node) {
        def course = findCourse(node)
        if (course != null && courseManagementService.canAccess(course)) {
            if (node instanceof Course) {
                def subjects = course.subjects
                ok item: SubjectVideo.findAllBySubjectInList(subjects).video
            } else if (node instanceof Subject) {
                ok item: node.videos
            } else if (node instanceof Video) {
                ok item: [node]
            } else {
                fail message: "Ukendt type", suggestedHttpStatus: 500
            }
        } else {
            fail message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403
        }
    }

    /**
     * Returns viewing statistics for a list of videos over some period of time.
     *
     * The views are additive in the way that, the output will count the total number of views for all of the videos,
     * it will return the individual views for each video.
     *
     * All videos must be owned by the authenticated user, otherwise this method will fail.
     *
     * The number of data points will always be 24.
     *
     * @param leaves The leaves to gather data from
     * @param periodFrom From when data should be gathered (timestamp [is milliseconds])
     * @param periodTo Until when data should be gathered (timestamp [is milliseconds])
     * @return Viewing statistics for the relevant nodes
     */
    ServiceResult<ViewingStatistics> findViewingStatistics(List<Video> leaves, Long periodFrom, Long periodTo,
                                                           Boolean cumulative) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())

        def ownsAllVideos = leaves
                .collect { findCourse(it) }
                .toSet()
                .stream()
                .allMatch { courseManagementService.canAccess(it) }

        if (!ownsAllVideos) {
            return fail(message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403)
        }

        List<VisitVideoEvent> events
        events = VisitVideoEvent.findAllByVideoIdInListAndTimestampBetween(videoIds, periodFrom, periodTo).findAll {
            it != null
        }
        long periodInMs = (periodTo - periodFrom) / 24

        List<String> labels = []
        List<Integer> data = []
        // Generate some labels (X-axis)
        long counter = periodFrom
        (0..23).each {
            labels.add(TIME_PATTERN.format(new Date(counter).toInstant().atZone(ZoneId.systemDefault())))
            data.add(0)
            counter += periodInMs
        }
        events.each {
            long time = it.timestamp
            int index = (time - periodFrom) / periodInMs
            if (index > 0) {
                data[index]++
            }
        }

        if (cumulative) {
            (1..23).each { index ->
                data[index] += data[index - 1]
            }
        }
        return ok(new ViewingStatistics(labels: labels, data: data))
    }

    /**
     * Finds the most popular videos for a given list of videos.
     *
     * The most popular videos are determined from the absolute number of hits a video gets. They will be returned
     * from in order from most popular to least popluar.
     *
     * The currently authenticated user must own all the videos, otherwise this method will fail.
     *
     * @param leaves The videos to retrieve statistics for
     * @param periodFrom From when data should be gathered (timestamp [is milliseconds])
     * @param periodTo Until when data should be gathered (timestamp [is milliseconds])
     * @return
     */
    ServiceResult<List<PopularVideoStatistic>> findPopularVideos(List<Video> leaves, Long periodFrom, Long periodTo) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())

        if (!videoIds) return fail("no videos")

        def ownsAllVideos = leaves
                .collect { findCourse(it) }
                .toSet()
                .stream()
                .allMatch { courseManagementService.canAccess(it) }

        if (!ownsAllVideos) {
            return fail(message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403)
        }

        String query = $/
            SELECT
                event.video_id,
                answers.answerCount,
                answers.correctAnswers,
                COUNT(*) AS visits
            FROM
                event LEFT OUTER JOIN (
                    SELECT
                        e.video_id,
                        COUNT(*) AS answerCount,
                        SUM(CASE e.correct WHEN TRUE THEN 1 ELSE 0 END) AS correctAnswers
                    FROM
                        event e
                    WHERE
                        e.class='dk.sdu.tekvideo.events.AnswerQuestionEvent' AND
                        e.timestamp >= :from_timestamp AND
                        e.timestamp <= :to_timestamp AND
                        e.video_id IN :video_ids
                    GROUP BY
                        e.video_id
                    ORDER BY
                        COUNT(*) DESC
                ) AS answers ON (event.video_id = answers.video_id)
            WHERE
                class='dk.sdu.tekvideo.events.VisitVideoEvent' AND
                event.video_id IN :video_ids AND
                event.timestamp >= :from_timestamp AND
                event.timestamp <= :to_timestamp
            GROUP BY
                event.video_id, answers.answerCount, answers.correctAnswers
            ORDER BY COUNT(*) DESC
            LIMIT 5;
            /$

        def resultList = sessionFactory.currentSession
                .createSQLQuery(query)
                .setParameterList("video_ids", videoIds)
                .setLong("from_timestamp", periodFrom)
                .setLong("to_timestamp", periodTo)
                .list()

        return ok(resultList.collect {
            def video = Video.get(it[0] as Long)

            new PopularVideoStatistic([
                    "videoId"       : it[0],
                    "videoName"     : video.name,
                    "subjectName"   : video.subject.name,
                    "courseName"    : video.subject.course.name,
                    "answerCount"   : it[1] ?: 0,
                    "correctAnswers": it[2] ?: 0,
                    "visits"        : it[3] ?: 0,
            ])
        })
    }

    ServiceResult<List<DetailedComment>> findRecentComments(List<Video> leaves, Long periodFrom, Long periodTo) {
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())

        if (!videoIds) return fail("no videos")
        if (periodFrom == null || periodTo == null || periodTo < periodFrom || periodFrom < 0 || periodTo < 0)
            return fail(message: "bad request", suggestedHttpStatus: HttpStatus.SC_BAD_REQUEST)

        def ownsAllVideos = leaves
                .collect { findCourse(it) }
                .toSet()
                .stream()
                .allMatch { courseManagementService.canAccess(it) }

        if (!ownsAllVideos) {
            return fail(message: "Du har ikke rettigheder til at tilgå dette kursus", suggestedHttpStatus: 403)
        }

        String query = $/
            SELECT
                myusers.username,
                user_id AS user_id,
                video.id AS video_id,
                video.name AS video_title,
                comment.date_created,
                contents,
                comment_id
            FROM
                comment,
                video_comment,
                myusers,
                video
            WHERE
                video_comment.comment_id = comment.id AND
                video_comment.video_comments_id IN :video_ids AND
                comment.date_created >= to_timestamp(:from_timestamp) AND
                comment.date_created <= to_timestamp(:to_timestamp) AND
                myusers.id = comment.user_id AND
                video.id = video_comment.video_comments_id
            ORDER BY
                video_comment.video_comments_id;
        /$

        def resultList = sessionFactory.currentSession
                .createSQLQuery(query)
                .setParameterList("video_ids", videoIds)
                .setLong("from_timestamp", (long) (periodFrom / 1000))
                .setLong("to_timestamp", (long) (periodTo / 1000))
                .list()

        return ok(resultList.collect {
            new DetailedComment([
                    "username"   : it[0],
                    "userId"     : it[1],
                    "videoId"    : it[2],
                    "videoTitle" : it[3],
                    "dateCreated": (it[4] as Timestamp).toInstant().epochSecond * 1000,
                    "comment"    : it[5],
                    "commentId"  : it[6],
                    "videoUrl"   : urlMappingService.generateLinkToVideo(Video.get(it[2]), [absolute: true])
            ])
        })
    }

    Set<Student> findStudents(Node node) {
        return CourseStudent.findAllByCourse(findCourse(node)).student // TODO Don't think this is efficient
    }

    ServiceResult<List<AnswerQuestionEvent>> getAnswers(Video video, Long periodFrom, Long periodTo) {
        if (video) {
            if (courseManagementService.canAccess(video.subject.course)) {
                ok item: AnswerQuestionEvent.findAllByVideoIdAndTimestampBetween(video.id, periodFrom, periodTo)
            } else {
                fail message: "You are not authorized to access this video!", suggestedHttpStatus: 403
            }
        } else {
            fail message: "Unknown video", suggestedHttpStatus: 404
        }
    }

    ServiceResult<List<Map>> findStudentActivity(Node node, List<Video> leaves, Long periodFrom, Long periodTo) {
        def course = findCourse(node)
        if (leaves == null) leaves = []
        def videoIds = leaves.stream().map { it.id }.collect(Collectors.toList())
        if (course) {
            String query = $/
                SELECT
                    course_students.username,
                    course_students.user_id,
                    course_students.elearn_id,
                    course_students.student_id,
                    answers.answer_count,
                    answers.correct_answers,
                    views.unique_views
                FROM
                  (
                    SELECT
                        myusers.username as username,
                        myusers.elearn_id as elearn_id,
                        myusers.id as user_id,
                        student.id as student_id
                    FROM
                        course,
                        course_student,
                        myusers,
                        student
                    WHERE
                        course.id = :course_id AND
                        course.id = course_student.course_id AND
                        student.id = course_student.student_id AND
                        myusers.id = student.user_id
                  ) AS course_students
                  LEFT OUTER JOIN
                  (
                    SELECT
                        user_id,
                        COUNT(*) AS answer_count,
                        SUM(CASE correct WHEN TRUE THEN 1 ELSE 0 END) AS correct_answers
                    FROM
                        event
                    WHERE
                        event.class = 'dk.sdu.tekvideo.events.AnswerQuestionEvent' AND
                        event.timestamp >= :from_timestamp AND
                        event.timestamp <= :to_timestamp AND
                        event.video_id IN :video_ids
                    GROUP BY
                        user_id
                  ) AS answers ON (answers.user_id = course_students.user_id)
                  LEFT OUTER JOIN
                  (
                    SELECT
                        user_id,
                        COUNT(DISTINCT video_id) AS unique_views
                    FROM
                        event
                    WHERE
                        event.class = 'dk.sdu.tekvideo.events.VisitVideoEvent' AND
                        event.timestamp >= :from_timestamp AND
                        event.timestamp <= :to_timestamp AND
                        event.video_id IN :video_ids
                    GROUP BY
                        user_id
                  ) AS views ON(answers.user_id = views.user_id);
            /$
            def resultList = sessionFactory.currentSession
                    .createSQLQuery(query)
                    .setParameterList("video_ids", videoIds)
                    .setLong("course_id", course.id)
                    .setLong("from_timestamp", periodFrom)
                    .setLong("to_timestamp", periodTo)
                    .list()

            ok item: resultList.collect {
                [
                        username      : it[0],
                        userId        : it[1],
                        elearnId      : it[2],
                        studentId     : it[3],
                        answerCount   : it[4] ?: 0,
                        correctAnswers: it[5] ?: 0,
                        uniqueViews   : it[6] ?: 0
                ]
            }
        } else {
            fail message: "Course not found!"
        }
    }
}
