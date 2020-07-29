import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGitInfo } from 'app/shared/model/git-info.model';

@Component({
  selector: 'jhi-git-info-detail',
  templateUrl: './git-info-detail.component.html',
})
export class GitInfoDetailComponent implements OnInit {
  gitInfo: IGitInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gitInfo }) => (this.gitInfo = gitInfo));
  }

  previousState(): void {
    window.history.back();
  }
}
