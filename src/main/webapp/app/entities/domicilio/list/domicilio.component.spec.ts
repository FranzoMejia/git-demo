import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DomicilioService } from '../service/domicilio.service';

import { DomicilioComponent } from './domicilio.component';

describe('Domicilio Management Component', () => {
  let comp: DomicilioComponent;
  let fixture: ComponentFixture<DomicilioComponent>;
  let service: DomicilioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DomicilioComponent],
    })
      .overrideTemplate(DomicilioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomicilioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DomicilioService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.domicilios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
