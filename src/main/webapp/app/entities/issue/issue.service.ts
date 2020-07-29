import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IIssue } from 'app/shared/model/issue.model';

type EntityResponseType = HttpResponse<IIssue>;
type EntityArrayResponseType = HttpResponse<IIssue[]>;

@Injectable({ providedIn: 'root' })
export class IssueService {
  public resourceUrl = SERVER_API_URL + 'api/issues';

  constructor(protected http: HttpClient) {}

  create(issue: IIssue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(issue);
    return this.http
      .post<IIssue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(issue: IIssue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(issue);
    return this.http
      .put<IIssue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIssue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIssue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(issue: IIssue): IIssue {
    const copy: IIssue = Object.assign({}, issue, {
      createdAt: issue.createdAt && issue.createdAt.isValid() ? issue.createdAt.toJSON() : undefined,
      updatedAt: issue.updatedAt && issue.updatedAt.isValid() ? issue.updatedAt.toJSON() : undefined,
      closedAt: issue.closedAt && issue.closedAt.isValid() ? issue.closedAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? moment(res.body.updatedAt) : undefined;
      res.body.closedAt = res.body.closedAt ? moment(res.body.closedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((issue: IIssue) => {
        issue.createdAt = issue.createdAt ? moment(issue.createdAt) : undefined;
        issue.updatedAt = issue.updatedAt ? moment(issue.updatedAt) : undefined;
        issue.closedAt = issue.closedAt ? moment(issue.closedAt) : undefined;
      });
    }
    return res;
  }
}
