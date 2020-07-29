import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGitInfo } from 'app/shared/model/git-info.model';

type EntityResponseType = HttpResponse<IGitInfo>;
type EntityArrayResponseType = HttpResponse<IGitInfo[]>;

@Injectable({ providedIn: 'root' })
export class GitInfoService {
  public resourceUrl = SERVER_API_URL + 'api/git-infos';

  constructor(protected http: HttpClient) {}

  create(gitInfo: IGitInfo): Observable<EntityResponseType> {
    return this.http.post<IGitInfo>(this.resourceUrl, gitInfo, { observe: 'response' });
  }

  update(gitInfo: IGitInfo): Observable<EntityResponseType> {
    return this.http.put<IGitInfo>(this.resourceUrl, gitInfo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGitInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGitInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
