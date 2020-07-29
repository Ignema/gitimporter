import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'git-info',
        loadChildren: () => import('./git-info/git-info.module').then(m => m.GitimporterGitInfoModule),
      },
      {
        path: 'issue',
        loadChildren: () => import('./issue/issue.module').then(m => m.GitimporterIssueModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GitimporterEntityModule {}
