export class DelItem {
    invoice: number;
    proSKU: string;
    movLine: number;
    proQty: number;

    constructor(invoice: number, proSKU: string, movLine: number, proQty: number){
        this.invoice = invoice
        this.proSKU = proSKU;
        this.movLine = movLine;
        this.proQty = proQty;
    }
}
