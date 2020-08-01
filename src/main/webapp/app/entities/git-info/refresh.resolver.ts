import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';

import { GitInfoService } from './git-info.service';

import { Observable, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

@Injectable()
export class RefreshResolver implements Resolve<Observable<string>> {
  constructor(private service: GitInfoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<string> {
    const name = route.params['name'];

    return this.service.refresh(name).pipe(
      flatMap((res: HttpResponse<string>) => {
        if (!res.body) {
          this.router.routeReuseStrategy.shouldReuseRoute = () => false;
          this.router.onSameUrlNavigation = 'reload';
          this.router.navigate(['/git-info']);

          return EMPTY;
        } else {
          this.router.navigate(['404']);
          return EMPTY;
        }
      })
    );
  }
}
