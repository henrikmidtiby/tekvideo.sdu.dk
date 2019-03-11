import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import './jquery-dependency.js';
import './tekvideo-math-field-scripts.js';

class TekvideoMathField extends PolymerElement {
  static get template() {
    return html`
<style>
:host {
    display: inline;
}

#mathField {
  min-width: var(--tekvideo-math-field-min-width, 300px);
}

/*
 * Modifications: escaped the content string for .mq-overarrow:before
 */
/*
 * MathQuill v0.10.1               http://mathquill.com
 * by Han, Jeanine, and Mary  maintainers@mathquill.com
 *
 * This Source Code Form is subject to the terms of the
 * Mozilla Public License, v. 2.0. If a copy of the MPL
 * was not distributed with this file, You can obtain
 * one at http://mozilla.org/MPL/2.0/.
 */
@font-face {
  font-family: Symbola;
  src: url(font/Symbola.eot);
  src: local("Symbola Regular"), local("Symbola"), url(font/Symbola.woff2) format("woff2"), url(font/Symbola.woff) format("woff"), url(font/Symbola.ttf) format("truetype"), url(font/Symbola.otf) format("opentype"), url(font/Symbola.svg#Symbola) format("svg");
}

.mq-editable-field {
  display: -moz-inline-box;
  display: inline-block;
}
.mq-editable-field .mq-cursor {
  border-left: 1px solid black;
  margin-left: -1px;
  position: relative;
  z-index: 1;
  padding: 0;
  display: -moz-inline-box;
  display: inline-block;
}
.mq-editable-field .mq-cursor.mq-blink {
  visibility: hidden;
}
.mq-editable-field,
.mq-math-mode .mq-editable-field {
  border: 1px solid gray;
}
.mq-editable-field.mq-focused,
.mq-math-mode .mq-editable-field.mq-focused {
  -webkit-box-shadow: #8bd 0 0 1px 2px, inset #6ae 0 0 2px 0;
  -moz-box-shadow: #8bd 0 0 1px 2px, inset #6ae 0 0 2px 0;
  box-shadow: #8bd 0 0 1px 2px, inset #6ae 0 0 2px 0;
  border-color: #709AC0;
  border-radius: 1px;
}
.mq-math-mode .mq-editable-field {
  margin: 1px;
}
.mq-editable-field .mq-latex-command-input {
  color: inherit;
  font-family: "Courier New", monospace;
  border: 1px solid gray;
  padding-right: 1px;
  margin-right: 1px;
  margin-left: 2px;
}
.mq-editable-field .mq-latex-command-input.mq-empty {
  background: transparent;
}
.mq-editable-field .mq-latex-command-input.mq-hasCursor {
  border-color: ActiveBorder;
}
.mq-editable-field.mq-empty:after,
.mq-editable-field.mq-text-mode:after,
.mq-math-mode .mq-empty:after {
  visibility: hidden;
  content: 'c';
}
.mq-editable-field .mq-cursor:only-child:after,
.mq-editable-field .mq-textarea + .mq-cursor:last-child:after {
  visibility: hidden;
  content: 'c';
}
.mq-editable-field .mq-text-mode .mq-cursor:only-child:after {
  content: '';
}
.mq-editable-field.mq-text-mode {
  overflow-x: auto;
  overflow-y: hidden;
}
.mq-root-block,
.mq-math-mode .mq-root-block {
  display: -moz-inline-box;
  display: inline-block;
  width: 100%;
  padding: 2px;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  white-space: nowrap;
  overflow: hidden;
  vertical-align: middle;
}
.mq-math-mode {
  font-variant: normal;
  font-weight: normal;
  font-style: normal;
  font-size: 115%;
  line-height: 1;
  display: -moz-inline-box;
  display: inline-block;
}
.mq-math-mode .mq-non-leaf,
.mq-math-mode .mq-scaled {
  display: -moz-inline-box;
  display: inline-block;
}
.mq-math-mode var,
.mq-math-mode .mq-text-mode,
.mq-math-mode .mq-nonSymbola {
  font-family: "Times New Roman", Symbola, serif;
  line-height: .9;
}
.mq-math-mode var,
.mq-math-mode .mq-text-mode,
.mq-math-mode .mq-nonSymbola {
  font-family: "Times New Roman", Symbola, serif;
  line-height: .9;
}
.mq-math-mode * {
  font-size: inherit;
  line-height: inherit;
  margin: 0;
  padding: 0;
  border-color: black;
  -webkit-user-select: none;
  -moz-user-select: none;
  user-select: none;
  box-sizing: border-box;
}
.mq-math-mode .mq-empty {
  background: #ccc;
}
.mq-math-mode .mq-empty.mq-root-block {
  background: transparent;
}
.mq-math-mode.mq-empty {
  background: transparent;
}
.mq-math-mode .mq-text-mode {
  display: inline-block;
}
.mq-math-mode .mq-text-mode.mq-hasCursor {
  box-shadow: inset darkgray 0 .1em .2em;
  padding: 0 .1em;
  margin: 0 -0.1em;
  min-width: 1ex;
}
.mq-math-mode .mq-font {
  font: 1em "Times New Roman", Symbola, serif;
}
.mq-math-mode .mq-font * {
  font-family: inherit;
  font-style: inherit;
}
.mq-math-mode b,
.mq-math-mode b.mq-font {
  font-weight: bolder;
}
.mq-math-mode var,
.mq-math-mode i,
.mq-math-mode i.mq-font {
  font-style: italic;
}
.mq-math-mode var.mq-f {
  margin-right: 0.2em;
  margin-left: 0.1em;
}
.mq-math-mode .mq-roman var.mq-f {
  margin: 0;
}
.mq-math-mode big {
  font-size: 200%;
}
.mq-math-mode .mq-int > big {
  display: inline-block;
  -webkit-transform: scaleX(0.7);
  -moz-transform: scaleX(0.7);
  -ms-transform: scaleX(0.7);
  -o-transform: scaleX(0.7);
  transform: scaleX(0.7);
  vertical-align: -0.16em;
}
.mq-math-mode .mq-int > .mq-supsub {
  font-size: 80%;
  vertical-align: -1.1em;
  padding-right: .2em;
}
.mq-math-mode .mq-int > .mq-supsub > .mq-sup > .mq-sup-inner {
  vertical-align: 1.3em;
}
.mq-math-mode .mq-int > .mq-supsub > .mq-sub {
  margin-left: -0.35em;
}
.mq-math-mode .mq-roman {
  font-style: normal;
}
.mq-math-mode .mq-sans-serif {
  font-family: sans-serif, Symbola, serif;
}
.mq-math-mode .mq-monospace {
  font-family: monospace, Symbola, serif;
}
.mq-math-mode .mq-overline {
  border-top: 1px solid black;
  margin-top: 1px;
}
.mq-math-mode .mq-underline {
  border-bottom: 1px solid black;
  margin-bottom: 1px;
}
.mq-math-mode .mq-binary-operator {
  padding: 0 0.2em;
  display: -moz-inline-box;
  display: inline-block;
}
.mq-math-mode .mq-supsub {
  text-align: left;
  font-size: 90%;
  vertical-align: -0.5em;
}
.mq-math-mode .mq-supsub.mq-sup-only {
  vertical-align: .5em;
}
.mq-math-mode .mq-supsub.mq-sup-only .mq-sup {
  display: inline-block;
  vertical-align: text-bottom;
}
.mq-math-mode .mq-supsub .mq-sup {
  display: block;
}
.mq-math-mode .mq-supsub .mq-sub {
  display: block;
  float: left;
}
.mq-math-mode .mq-supsub .mq-binary-operator {
  padding: 0 .1em;
}
.mq-math-mode .mq-supsub .mq-fraction {
  font-size: 70%;
}
.mq-math-mode sup.mq-nthroot {
  font-size: 80%;
  vertical-align: 0.8em;
  margin-right: -0.6em;
  margin-left: .2em;
  min-width: .5em;
}
.mq-math-mode .mq-paren {
  padding: 0 .1em;
  vertical-align: top;
  -webkit-transform-origin: center .06em;
  -moz-transform-origin: center .06em;
  -ms-transform-origin: center .06em;
  -o-transform-origin: center .06em;
  transform-origin: center .06em;
}
.mq-math-mode .mq-paren.mq-ghost {
  color: silver;
}
.mq-math-mode .mq-paren + span {
  margin-top: .1em;
  margin-bottom: .1em;
}
.mq-math-mode .mq-array {
  vertical-align: middle;
  text-align: center;
}
.mq-math-mode .mq-array > span {
  display: block;
}
.mq-math-mode .mq-operator-name {
  font-family: Symbola, "Times New Roman", serif;
  line-height: .9;
  font-style: normal;
}
.mq-math-mode var.mq-operator-name.mq-first {
  padding-left: .2em;
}
.mq-math-mode var.mq-operator-name.mq-last,
.mq-math-mode .mq-supsub.mq-after-operator-name {
  padding-right: .2em;
}
.mq-math-mode .mq-fraction {
  font-size: 90%;
  text-align: center;
  vertical-align: -0.4em;
  padding: 0 .2em;
}
.mq-math-mode .mq-fraction,
.mq-math-mode .mq-large-operator,
.mq-math-mode x:-moz-any-link {
  display: -moz-groupbox;
}
.mq-math-mode .mq-fraction,
.mq-math-mode .mq-large-operator,
.mq-math-mode x:-moz-any-link,
.mq-math-mode x:default {
  display: inline-block;
}
.mq-math-mode .mq-numerator,
.mq-math-mode .mq-denominator {
  display: block;
}
.mq-math-mode .mq-numerator {
  padding: 0 0.1em;
}
.mq-math-mode .mq-denominator {
  border-top: 1px solid;
  float: right;
  width: 100%;
  padding: 0.1em;
}
.mq-math-mode .mq-sqrt-prefix {
  padding-top: 0;
  position: relative;
  top: 0.1em;
  vertical-align: top;
  -webkit-transform-origin: top;
  -moz-transform-origin: top;
  -ms-transform-origin: top;
  -o-transform-origin: top;
  transform-origin: top;
}
.mq-math-mode .mq-sqrt-stem {
  border-top: 1px solid;
  margin-top: 1px;
  padding-left: .15em;
  padding-right: .2em;
  margin-right: .1em;
  padding-top: 1px;
}
.mq-math-mode .mq-vector-prefix {
  display: block;
  text-align: center;
  line-height: .25em;
  margin-bottom: -0.1em;
  font-size: 0.75em;
}
.mq-math-mode .mq-vector-stem {
  display: block;
}
.mq-math-mode .mq-large-operator {
  vertical-align: -0.2em;
  padding: .2em;
  text-align: center;
}
.mq-math-mode .mq-large-operator .mq-from,
.mq-math-mode .mq-large-operator big,
.mq-math-mode .mq-large-operator .mq-to {
  display: block;
}
.mq-math-mode .mq-large-operator .mq-from,
.mq-math-mode .mq-large-operator .mq-to {
  font-size: 80%;
}
.mq-math-mode .mq-large-operator .mq-from {
  float: right;
  /* take out of normal flow to manipulate baseline */
  width: 100%;
}
.mq-math-mode,
.mq-math-mode .mq-editable-field {
  cursor: text;
  font-family: Symbola, "Times New Roman", serif;
}
.mq-math-mode .mq-overarrow {
  border-top: 1px solid black;
  margin-top: 1px;
  padding-top: 0.2em;
}
.mq-math-mode .mq-overarrow:before {
  display: block;
  position: relative;
  top: -0.34em;
  font-size: 0.5em;
  line-height: 0em;
  content: '\\27A4';
  text-align: right;
}
.mq-math-mode .mq-overarrow.mq-arrow-left:before {
  -moz-transform: scaleX(-1);
  -o-transform: scaleX(-1);
  -webkit-transform: scaleX(-1);
  transform: scaleX(-1);
  filter: FlipH;
  -ms-filter: "FlipH";
}
.mq-math-mode .mq-selection,
.mq-editable-field .mq-selection,
.mq-math-mode .mq-selection .mq-non-leaf,
.mq-editable-field .mq-selection .mq-non-leaf,
.mq-math-mode .mq-selection .mq-scaled,
.mq-editable-field .mq-selection .mq-scaled {
  background: #B4D5FE !important;
  background: Highlight !important;
  color: HighlightText;
  border-color: HighlightText;
}
.mq-math-mode .mq-selection .mq-matrixed,
.mq-editable-field .mq-selection .mq-matrixed {
  background: #39F !important;
}
.mq-math-mode .mq-selection .mq-matrixed-container,
.mq-editable-field .mq-selection .mq-matrixed-container {
  filter: progid:DXImageTransform.Microsoft.Chroma(color='#3399FF') !important;
}
.mq-math-mode .mq-selection.mq-blur,
.mq-editable-field .mq-selection.mq-blur,
.mq-math-mode .mq-selection.mq-blur .mq-non-leaf,
.mq-editable-field .mq-selection.mq-blur .mq-non-leaf,
.mq-math-mode .mq-selection.mq-blur .mq-scaled,
.mq-editable-field .mq-selection.mq-blur .mq-scaled,
.mq-math-mode .mq-selection.mq-blur .mq-matrixed,
.mq-editable-field .mq-selection.mq-blur .mq-matrixed {
  background: #D4D4D4 !important;
  color: black;
  border-color: black;
}
.mq-math-mode .mq-selection.mq-blur .mq-matrixed-container,
.mq-editable-field .mq-selection.mq-blur .mq-matrixed-container {
  filter: progid:DXImageTransform.Microsoft.Chroma(color='#D4D4D4') !important;
}
.mq-editable-field .mq-textarea,
.mq-math-mode .mq-textarea {
  position: relative;
  -webkit-user-select: text;
  -moz-user-select: text;
  user-select: text;
}
.mq-editable-field .mq-textarea *,
.mq-math-mode .mq-textarea *,
.mq-editable-field .mq-selectable,
.mq-math-mode .mq-selectable {
  -webkit-user-select: text;
  -moz-user-select: text;
  user-select: text;
  position: absolute;
  clip: rect(1em 1em 1em 1em);
  -webkit-transform: scale(0);
  -moz-transform: scale(0);
  -ms-transform: scale(0);
  -o-transform: scale(0);
  transform: scale(0);
  resize: none;
  width: 1px;
  height: 1px;
}
.mq-math-mode .mq-matrixed {
  background: white;
  display: -moz-inline-box;
  display: inline-block;
}
.mq-math-mode .mq-matrixed-container {
  filter: progid:DXImageTransform.Microsoft.Chroma(color='white');
  margin-top: -0.1em;
}


</style>

<span id="editor"></span>

`;
  }

  static get properties() {
    return {
      value: {
          type: String,
          value: "",
          observer: "_valueChanged"
      }
    }
  }

  static get is() {
    return "tekvideo-math-field";
  }

  constructor() {
    super();
    this._shouldUpdate = true;
    this._mathField = null;
  }

  ready() {
      super.ready();
      var self = this;
      var editor = this.$.editor;
      editor.classList.add(this.is);
      var MQ = MathQuill.getInterface(2);
      var mathField = MQ.MathField(editor, {
          spaceBehavesLikeTab: true,
          handlers: {
              edit: function() {
                  self._shouldUpdate = false;
                  self.value = mathField.latex();
              }
          }
      });
      this._mathField = mathField;
  }

  _valueChanged() {
      if (this._shouldUpdate && this._mathField != null) {
          this._mathField.latex(this.value);
      }
      this._shouldUpdate = true;
  }
}

customElements.define('tekvideo-math-field', TekvideoMathField);
