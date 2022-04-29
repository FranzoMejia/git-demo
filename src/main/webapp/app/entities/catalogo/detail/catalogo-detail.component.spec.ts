import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogoDetailComponent } from './catalogo-detail.component';

describe('Catalogo Management Detail Component', () => {
  let comp: CatalogoDetailComponent;
  let fixture: ComponentFixture<CatalogoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CatalogoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ catalogo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CatalogoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CatalogoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load catalogo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.catalogo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
