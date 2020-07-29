import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GitimporterTestModule } from '../../../test.module';
import { GitInfoDetailComponent } from 'app/entities/git-info/git-info-detail.component';
import { GitInfo } from 'app/shared/model/git-info.model';

describe('Component Tests', () => {
  describe('GitInfo Management Detail Component', () => {
    let comp: GitInfoDetailComponent;
    let fixture: ComponentFixture<GitInfoDetailComponent>;
    const route = ({ data: of({ gitInfo: new GitInfo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GitimporterTestModule],
        declarations: [GitInfoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(GitInfoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GitInfoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load gitInfo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.gitInfo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
