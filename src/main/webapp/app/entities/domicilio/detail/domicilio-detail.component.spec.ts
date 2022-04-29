import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DomicilioDetailComponent } from './domicilio-detail.component';

describe('Domicilio Management Detail Component', () => {
  let comp: DomicilioDetailComponent;
  let fixture: ComponentFixture<DomicilioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DomicilioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ domicilio: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DomicilioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DomicilioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load domicilio on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.domicilio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
