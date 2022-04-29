import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDomicilio, Domicilio } from '../domicilio.model';

import { DomicilioService } from './domicilio.service';

describe('Domicilio Service', () => {
  let service: DomicilioService;
  let httpMock: HttpTestingController;
  let elemDefault: IDomicilio;
  let expectedResult: IDomicilio | IDomicilio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DomicilioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      descripcion: 'AAAAAAA',
      ubicacion: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Domicilio', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Domicilio()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Domicilio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descripcion: 'BBBBBB',
          ubicacion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Domicilio', () => {
      const patchObject = Object.assign(
        {
          descripcion: 'BBBBBB',
          ubicacion: 'BBBBBB',
        },
        new Domicilio()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Domicilio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descripcion: 'BBBBBB',
          ubicacion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Domicilio', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDomicilioToCollectionIfMissing', () => {
      it('should add a Domicilio to an empty array', () => {
        const domicilio: IDomicilio = { id: 123 };
        expectedResult = service.addDomicilioToCollectionIfMissing([], domicilio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domicilio);
      });

      it('should not add a Domicilio to an array that contains it', () => {
        const domicilio: IDomicilio = { id: 123 };
        const domicilioCollection: IDomicilio[] = [
          {
            ...domicilio,
          },
          { id: 456 },
        ];
        expectedResult = service.addDomicilioToCollectionIfMissing(domicilioCollection, domicilio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Domicilio to an array that doesn't contain it", () => {
        const domicilio: IDomicilio = { id: 123 };
        const domicilioCollection: IDomicilio[] = [{ id: 456 }];
        expectedResult = service.addDomicilioToCollectionIfMissing(domicilioCollection, domicilio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domicilio);
      });

      it('should add only unique Domicilio to an array', () => {
        const domicilioArray: IDomicilio[] = [{ id: 123 }, { id: 456 }, { id: 17665 }];
        const domicilioCollection: IDomicilio[] = [{ id: 123 }];
        expectedResult = service.addDomicilioToCollectionIfMissing(domicilioCollection, ...domicilioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const domicilio: IDomicilio = { id: 123 };
        const domicilio2: IDomicilio = { id: 456 };
        expectedResult = service.addDomicilioToCollectionIfMissing([], domicilio, domicilio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domicilio);
        expect(expectedResult).toContain(domicilio2);
      });

      it('should accept null and undefined values', () => {
        const domicilio: IDomicilio = { id: 123 };
        expectedResult = service.addDomicilioToCollectionIfMissing([], null, domicilio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domicilio);
      });

      it('should return initial array if no Domicilio is added', () => {
        const domicilioCollection: IDomicilio[] = [{ id: 123 }];
        expectedResult = service.addDomicilioToCollectionIfMissing(domicilioCollection, undefined, null);
        expect(expectedResult).toEqual(domicilioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
