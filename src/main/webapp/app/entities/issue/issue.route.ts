import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IIssue, Issue } from 'app/shared/model/issue.model';
import { IssueService } from './issue.service';
import { IssueComponent } from './issue.component';
import { IssueDetailComponent } from './issue-detail.component';
import { IssueUpdateComponent } from './issue-update.component';

@Injectable({ providedIn: 'root' })
export class IssueResolve implements Resolve<IIssue> {
  constructor(private service: IssueService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIssue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((issue: HttpResponse<Issue>) => {
          if (issue.body) {
            return of(issue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Issue());
  }
}

export const issueRoute: Routes = [
  {
    path: '',
    component: IssueComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gitimporterApp.issue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IssueDetailComponent,
    resolve: {
      issue: IssueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gitimporterApp.issue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IssueUpdateComponent,
    resolve: {
      issue: IssueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gitimporterApp.issue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IssueUpdateComponent,
    resolve: {
      issue: IssueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gitimporterApp.issue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
