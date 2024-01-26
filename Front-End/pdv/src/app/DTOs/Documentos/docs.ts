export class Docs {
    invoice:number;
    date:Date;
    code:string;
    client:string;
    qty:number;
    net:number;
    disc:number;
    subtotal:number;
    tax: number;
    total: number;
    status: number; //0 = Pendiente, 1 = Cerrado, 2 = Cancelado
    statusText: string;

    constructor(invoice: number, date: Date, code:string,client:string,qty:number,
        net:number,disc:number,subtotal:number,tax: number,total: number,status: number, statusText: string) {
        this.invoice = invoice;
        this.date = date;
        this.code = code;
        this.client = client;
        this.qty = qty;
        this.net = net;
        this.disc = disc;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.status = status;
        this.statusText = statusText;
      }
}
