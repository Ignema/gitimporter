import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGitInfo, GitInfo } from 'app/shared/model/git-info.model';
import { GitInfoService } from './git-info.service';
import { GitInfoComponent } from './git-info.component';
import { GitInfoDetailComponent } from './git-info-detail.component';
import { GitInfoUpdateComponent } from './git-info-update.component';

@Injectable({ providedIn: 'root' })
export class GitInfoResolve implements Resolve<IGitInfo> {
  constructor(private service: GitInfoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGitInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((gitInfo: HttpResponse<GitInfo>) => {
          if (gitInfo.body) {
            return of(gitInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GitInfo());
  }
}

export const gitInfoRoute: Routes = [
  {
    path: '',
    component: GitInfoComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gitimporterApp.gitInfo.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GitInfoDetailComponent,
    resolve: {
      gitInfo: GitInfoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gitimporterApp.gitInfo.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GitInfoUpdateComponent,
    resolve: {
      gitInfo: GitInfoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gitimporterApp.gitInfo.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GitInfoUpdateComponent,
    resolve: {
      gitInfo: GitInfoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gitimporterApp.gitInfo.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
