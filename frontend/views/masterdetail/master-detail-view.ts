import { customElement, html, LitElement, query, unsafeCSS, property } from 'lit-element';

import '@vaadin/vaadin-button/vaadin-button';
import '@vaadin/vaadin-form-layout/vaadin-form-item';
import '@vaadin/vaadin-form-layout/vaadin-form-layout';
import '@vaadin/vaadin-grid/vaadin-grid.js';
import { GridElement } from '@vaadin/vaadin-grid/vaadin-grid';
import '@vaadin/vaadin-grid/vaadin-grid-column';
import '@vaadin/vaadin-notification/vaadin-notification.js';
import { NotificationElement } from '@vaadin/vaadin-notification/vaadin-notification';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-split-layout/vaadin-split-layout';
import '@vaadin/vaadin-combo-box/vaadin-combo-box';
import '@vaadin/vaadin-text-field/vaadin-text-field';

import { Binder, field } from '@vaadin/form';

// import the remote endpoint
import { getCompanies, getEmployees, saveEmployee } from '../../generated/MasterDetailEndpoint';

// import types used in the endpoint
import Contact from '../../generated/org/vaadin/example/backend/entity/Contact';
import Company from '../../generated/org/vaadin/example/backend/entity/Company';


import { EndpointError } from '@vaadin/flow-frontend/Connect';

// utilities to import style modules
import { CSSModule } from '../../css-utils';

// @ts-ignore
import styles from './master-detail-view.css';
import ContactModel from '../../generated/org/vaadin/example/backend/entity/ContactModel';

@customElement('master-detail-view')
export class MasterDetailViewElement extends LitElement {
  static get styles() {
    return [CSSModule('lumo-typography'), unsafeCSS(styles)];
  }

  @query('#grid')
  private grid!: GridElement;

  @query('#notification')
  private notification!: NotificationElement;

  @property({ type: Array }) contacts: Contact[] = [];

  @property({ type: Array }) companies: Company[] = [];


  private binder = new Binder(this, ContactModel);


  render() {
    return html`
      <vaadin-split-layout class="splitLayout">
        <div class="splitLayout__gridTable">
          <vaadin-grid id="grid" class="splitLayout" theme="no-border"
            @active-item-changed="${this.activeItemChanged}">
            <vaadin-grid-column
              header="First name"
              path="firstName"
            ></vaadin-grid-column>
            <vaadin-grid-column
              header="Last name"
              path="lastName"
            ></vaadin-grid-column>
            <vaadin-grid-column
              header="Email"
              path="email"
            ></vaadin-grid-column>
          </vaadin-grid>
        </div>
        <div id="editor-layout">
          <vaadin-form-layout>
            <vaadin-form-item>
              <label slot="label">First name</label>
              <vaadin-text-field
                class="full-width"
                id="firstName"
                ...="${field(this.binder.model.firstName)}"
              ></vaadin-text-field>
            </vaadin-form-item>
            <vaadin-form-item>
              <label slot="label">Last name</label>
              <vaadin-text-field
                class="full-width"
                id="lastName"
                ...="${field(this.binder.model.lastName)}"
              ></vaadin-text-field>
            </vaadin-form-item>
            <vaadin-form-item>
              <label slot="label">Email</label>
              <vaadin-text-field
                class="full-width"
                id="email"
                ...="${field(this.binder.model.email)}"
              ></vaadin-text-field>
            </vaadin-form-item>
            <vaadin-form-item>
              <label slot="label">Company</label>
              <vaadin-combo-box
                class="full-width"
                id="company"
                item-label-path="name"
                item-value-path="id"
                .items="${this.companies}"
                ...="${field(this.binder.model.company.id)}"
              >
              </vaadin-combo-box>
            </vaadin-form-item>
          </vaadin-form-layout>
          <vaadin-horizontal-layout id="button-layout" theme="spacing">
            <vaadin-button theme="tertiary" slot="" @click="${this.clearForm}">
              Cancel
            </vaadin-button>
            <vaadin-button theme="primary" @click="${this.save}" ?disabled="${this.binder.invalid || this.binder.submitting}">
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

    
    this.companies = await getCompanies();

    this.grid.dataProvider = async (params, callback) => {
      // Retrieve data from the server-side endpoint.
      const result = await getEmployees(params.page, params.pageSize);
      callback(result.pageItems, result.totalCount);
    };
  }

  private activeItemChanged(event: any) {
    const item = event.detail.value;
    if (item) {
      this.binder.read(item);
    } else {
      this.binder.clear();
    }
    this.grid.selectedItems = item ? [item] : [];
  }

  private async save() {
    try {
      await this.binder.submitTo(saveEmployee);
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
    this.binder.clear();
    this.grid.selectedItems = [];
  }
}
