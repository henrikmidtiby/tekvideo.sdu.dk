package dk.sdu.tekvideo

import grails.validation.Validateable

/**
 * @author Dan Thrane
 */
@Validateable
class SubjectCRUDCommand {
    Subject domain
    Boolean isEditing
    Boolean visible
}
