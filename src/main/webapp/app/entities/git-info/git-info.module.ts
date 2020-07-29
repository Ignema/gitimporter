import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GitimporterSharedModule } from 'app/shared/shared.module';
import { GitInfoComponent } from './git-info.component';
import { GitInfoDetailComponent } from './git-info-detail.component';
import { GitInfoUpdateComponent } from './git-info-update.component';
import { GitInfoDeleteDialogComponent } from './git-info-delete-dialog.component';
import { gitInfoRoute } from './git-info.route';

@NgModule({
  imports: [GitimporterSharedModule, RouterModule.forChild(gitInfoRoute)],
  declarations: [GitInfoComponent, GitInfoDetailComponent, GitInfoUpdateComponent, GitInfoDeleteDialogComponent],
  entryComponents: [GitInfoDeleteDialogComponent],
})
export class GitimporterGitInfoModule {}
