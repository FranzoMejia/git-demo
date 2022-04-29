import dayjs from 'dayjs/esm';
import { IDomicilio } from 'app/entities/domicilio/domicilio.model';
import { IProducto } from 'app/entities/producto/producto.model';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IPedido {
  id?: number;
  total?: number | null;
  fechaGenerado?: dayjs.Dayjs | null;
  fechaEntrega?: dayjs.Dayjs | null;
  preparado?: boolean | null;
  entregado?: boolean | null;
  foraneo?: boolean | null;
  domicilio?: IDomicilio | null;
  productos?: IProducto[] | null;
  cliente?: ICliente | null;
}

export class Pedido implements IPedido {
  constructor(
    public id?: number,
    public total?: number | null,
    public fechaGenerado?: dayjs.Dayjs | null,
    public fechaEntrega?: dayjs.Dayjs | null,
    public preparado?: boolean | null,
    public entregado?: boolean | null,
    public foraneo?: boolean | null,
    public domicilio?: IDomicilio | null,
    public productos?: IProducto[] | null,
    public cliente?: ICliente | null
  ) {
    this.preparado = this.preparado ?? false;
    this.entregado = this.entregado ?? false;
    this.foraneo = this.foraneo ?? false;
  }
}

export function getPedidoIdentifier(pedido: IPedido): number | undefined {
  return pedido.id;
}
