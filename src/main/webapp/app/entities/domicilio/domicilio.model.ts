import { IPedido } from 'app/entities/pedido/pedido.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IDomicilio {
  id?: number;
  descripcion?: string;
  ubicacion?: string | null;
  pedido?: IPedido | null;
  cliente?: ICliente | null;
}

export class Domicilio implements IDomicilio {
  constructor(
    public id?: number,
    public descripcion?: string,
    public ubicacion?: string | null,
    public pedido?: IPedido | null,
    public cliente?: ICliente | null
  ) {}
}

export function getDomicilioIdentifier(domicilio: IDomicilio): number | undefined {
  return domicilio.id;
}
