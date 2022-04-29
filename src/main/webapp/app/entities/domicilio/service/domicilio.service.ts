import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDomicilio, getDomicilioIdentifier } from '../domicilio.model';

export type EntityResponseType = HttpResponse<IDomicilio>;
export type EntityArrayResponseType = HttpResponse<IDomicilio[]>;

@Injectable({ providedIn: 'root' })
export class DomicilioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/domicilios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(domicilio: IDomicilio): Observable<EntityResponseType> {
    return this.http.post<IDomicilio>(this.resourceUrl, domicilio, { observe: 'response' });
  }

  update(domicilio: IDomicilio): Observable<EntityResponseType> {
    return this.http.put<IDomicilio>(`${this.resourceUrl}/${getDomicilioIdentifier(domicilio) as number}`, domicilio, {
      observe: 'response',
    });
  }

  partialUpdate(domicilio: IDomicilio): Observable<EntityResponseType> {
    return this.http.patch<IDomicilio>(`${this.resourceUrl}/${getDomicilioIdentifier(domicilio) as number}`, domicilio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDomicilio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDomicilio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDomicilioToCollectionIfMissing(
    domicilioCollection: IDomicilio[],
    ...domiciliosToCheck: (IDomicilio | null | undefined)[]
  ): IDomicilio[] {
    const domicilios: IDomicilio[] = domiciliosToCheck.filter(isPresent);
    if (domicilios.length > 0) {
      const domicilioCollectionIdentifiers = domicilioCollection.map(domicilioItem => getDomicilioIdentifier(domicilioItem)!);
      const domiciliosToAdd = domicilios.filter(domicilioItem => {
        const domicilioIdentifier = getDomicilioIdentifier(domicilioItem);
        if (domicilioIdentifier == null || domicilioCollectionIdentifiers.includes(domicilioIdentifier)) {
          return false;
        }
        domicilioCollectionIdentifiers.push(domicilioIdentifier);
        return true;
      });
      return [...domiciliosToAdd, ...domicilioCollection];
    }
    return domicilioCollection;
  }
}
