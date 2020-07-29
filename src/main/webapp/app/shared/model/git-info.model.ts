export interface IGitInfo {
  id?: number;
  gitProjectId?: number;
  gitProjectName?: string;
  issueCount?: number;
}

export class GitInfo implements IGitInfo {
  constructor(public id?: number, public gitProjectId?: number, public gitProjectName?: string, public issueCount?: number) {}
}
