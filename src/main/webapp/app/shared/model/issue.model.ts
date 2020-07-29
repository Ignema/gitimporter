import { Moment } from 'moment';

export interface IIssue {
  id?: number;
  issueId?: number;
  issueOrder?: number;
  issueTitle?: string;
  state?: boolean;
  author?: string;
  description?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  closedAt?: Moment;
  closedBy?: string;
}

export class Issue implements IIssue {
  constructor(
    public id?: number,
    public issueId?: number,
    public issueOrder?: number,
    public issueTitle?: string,
    public state?: boolean,
    public author?: string,
    public description?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public closedAt?: Moment,
    public closedBy?: string
  ) {
    this.state = this.state || false;
  }
}
