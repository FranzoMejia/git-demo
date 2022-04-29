import { IDomicilio } from 'app/entities/domicilio/domicilio.model';
import { IPedido } from 'app/entities/pedido/pedido.model';

export interface ICliente {
  id?: number;
  nombre?: string;
  telefono?: string | null;
  domicilios?: IDomicilio[] | null;
  pedidos?: IPedido[] | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string,
    public telefono?: string | null,
    public domicilios?: IDomicilio[] | null,
    public pedidos?: IPedido[] | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
