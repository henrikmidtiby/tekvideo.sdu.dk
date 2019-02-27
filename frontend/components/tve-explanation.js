import '@polymer/polymer/polymer-legacy.js';
import '@polymer/iron-flex-layout/iron-flex-layout-classes.js';
import '@polymer/marked-element/marked-element.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/iron-collapse/iron-collapse.js';
import './tve-renderer.js';
Polymer({
  _template: Polymer.html`
<style include="iron-flex"></style>

<style>
:host {
    display: block;
}
</style>

<paper-button id="button" on-click="showExplanation" noink="">Vis hint</paper-button>

<iron-collapse id="collapse" no-animation="">
    <h3>Hint</h3>
    <tve-renderer content="{{content}}"></tve-renderer>
</iron-collapse>
`,

  is: 'tve-explanation',

  properties: {
      content: {
          type: String,
          value: ""
      }
  },

  showExplanation: function() {
      this.$.collapse.show();
      this.$.button.hidden = true;

      this.fire("action", { type: "hint-showed" })
  }
});
