import { customElement, html, LitElement, query, unsafeCSS } from 'lit-element';

import '@vaadin/vaadin-button/vaadin-button';
import '@vaadin/vaadin-form-layout/vaadin-form-item';
import '@vaadin/vaadin-form-layout/vaadin-form-layout';
import '@vaadin/vaadin-grid/vaadin-grid';
import '@vaadin/vaadin-grid/vaadin-grid-column';
import '@vaadin/vaadin-notification/vaadin-notification';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-split-layout/vaadin-split-layout';
import '@vaadin/vaadin-combo-box/vaadin-combo-box';
import '@vaadin/vaadin-text-field/vaadin-text-field';

// import the remote endpoint
import * as viewEndpoint from '../../generated/MasterDetailEndpoint';

// import types used in the endpoint
import Contact from '../../generated/org/vaadin/example/backend/entity/Contact';

import { EndpointError } from '@vaadin/flow-frontend/Connect';

// utilities to import style modules
import { CSSModule } from '../../css-utils';

// @ts-ignore
import styles from './master-detail-view.css';

@customElement('master-detail-view')
export class MasterDetailViewElement extends LitElement {
  static get styles() {
    return [CSSModule('lumo-typography'), unsafeCSS(styles)];
  }

  @query('#grid')
  private grid: any;

  @query('#notification')
  private notification: any;

  @query('#firstName') private firstName: any;
  @query('#lastName') private lastName: any;
  @query('#email') private email: any;
  @query('#company') private company: any;
  private employeeId: any;

  render() {
    return html`
      <vaadin-split-layout class="splitLayout">
        <div class="splitLayout__gridTable">
          <vaadin-grid id="grid" class="splitLayout" theme="no-border">
            <vaadin-grid-column header="First name" path="firstName"></vaadin-grid-column>
            <vaadin-grid-column header="Last name" path="lastName"></vaadin-grid-column>
            <vaadin-grid-column header="Email" path="email"></vaadin-grid-column>
          </vaadin-grid>
        </div>
        <div id="editor-layout">
          <vaadin-form-layout>
            <vaadin-form-item>
              <label slot="label">First name</label>
              <vaadin-text-field
                class="full-width"
                id="firstName"
              ></vaadin-text-field>
            </vaadin-form-item>
            <vaadin-form-item>
              <label slot="label">Last name</label>
              <vaadin-text-field
                class="full-width"
                id="lastName"
              ></vaadin-text-field>
            </vaadin-form-item>
            <vaadin-form-item>
              <label slot="label">Email</label>
              <vaadin-text-field
                class="full-width"
                id="email"
              ></vaadin-text-field>
            </vaadin-form-item>
            <vaadin-form-item>
              <label slot="label">Company</label>
              <vaadin-combo-box class="full-width" id="company" item-label-path="name" item-value-path="id">
              </vaadin-combo-box>
            </vaadin-form-item>
          </vaadin-form-layout>
          <vaadin-horizontal-layout id="button-layout" theme="spacing">
            <vaadin-button theme="tertiary" slot="" @click="${this.clearForm}">
              Cancel
            </vaadin-button>
            <vaadin-button theme="primary" @click="${this.save}">
              Save
            </vaadin-button>
          </vaadin-horizontal-layout>
        </div>
      </vaadin-split-layout>
      <vaadin-notification duration="0" id="notification">
      </vaadin-notification>
    `;
  }

  // Wait until all elements in the template are ready to set their properties
  async firstUpdated(changedProperties: any) {
    super.firstUpdated(changedProperties);

    // Retrieve data from the server-side endpoint.
    const persons = await viewEndpoint.getEmployees();
    this.grid.items = persons;
    this.grid.addEventListener('active-item-changed', function(
      this: any,
      event: any
    ) {
      const item = event.detail.value;
      this.selectedItems = item ? [item] : [];
      const customView = this.domHost;

      if (item) {
        customView.firstName.value = item.firstName;
        customView.lastName.value = item.lastName;
        customView.email.value = item.email;
        customView.employeeId = item.id;
        customView.company.value = item.company.id;
      } else {
        customView.clearForm();
      }
    });

    const companies = await viewEndpoint.getCompanies();
    this.company.items = companies;
  }

  private async save() {
    const contact: Contact = {
      id: this.employeeId,
      email: this.email.value,
      firstName: this.firstName.value,
      lastName: this.lastName.value,
      company: this.company.selectedItem
    };
    try {
      await viewEndpoint.saveEmployee(contact);
      const index = this.grid.items.findIndex(
        (item: Contact) => item.id === contact.id
      );
      if (index >= 0) {
        this.grid.items[index] = contact;
        this.grid.clearCache();
      }
    } catch (error) {
      if (error instanceof EndpointError) {
        this.notification.renderer = (root: Element) =>
          (root.textContent = `Server error. ${error.message}`);
        this.notification.open();
      } else {
        throw error;
      }
    }
  }

  private clearForm() {
    this.grid.selectedItems = [];
    this.firstName.value = '';
    this.lastName.value = '';
    this.email.value = '';
    this.employeeId = '';
    this.company.value = null;
  }
}
