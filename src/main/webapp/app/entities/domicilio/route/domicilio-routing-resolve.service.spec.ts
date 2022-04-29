import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDomicilio, Domicilio } from '../domicilio.model';
import { DomicilioService } from '../service/domicilio.service';

import { DomicilioRoutingResolveService } from './domicilio-routing-resolve.service';

describe('Domicilio routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DomicilioRoutingResolveService;
  let service: DomicilioService;
  let resultDomicilio: IDomicilio | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(DomicilioRoutingResolveService);
    service = TestBed.inject(DomicilioService);
    resultDomicilio = undefined;
  });

  describe('resolve', () => {
    it('should return IDomicilio returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomicilio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDomicilio).toEqual({ id: 123 });
    });

    it('should return new IDomicilio if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomicilio = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDomicilio).toEqual(new Domicilio());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Domicilio })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomicilio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDomicilio).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
