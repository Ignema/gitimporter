import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GitimporterSharedModule } from 'app/shared/shared.module';
import { IssueComponent } from './issue.component';
import { IssueDetailComponent } from './issue-detail.component';
import { IssueUpdateComponent } from './issue-update.component';
import { IssueDeleteDialogComponent } from './issue-delete-dialog.component';
import { issueRoute } from './issue.route';

@NgModule({
  imports: [GitimporterSharedModule, RouterModule.forChild(issueRoute)],
  declarations: [IssueComponent, IssueDetailComponent, IssueUpdateComponent, IssueDeleteDialogComponent],
  entryComponents: [IssueDeleteDialogComponent],
})
export class GitimporterIssueModule {}
