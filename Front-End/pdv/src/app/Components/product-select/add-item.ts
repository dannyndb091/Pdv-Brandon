export class AddItem {
    invoice: number;
    clCode: string;
    proSKU: string;
    proQty: number;
    movPU: number;
    movNet: number;
    movDisc: number;
    movSubtotal: number;
    movTax: number;
    movTotal: number;
    movLine: number;

    constructor(invoice: number, clCode: string, proSKU: string, proQty: number, movPU: number, 
        movNet: number, movDisc: number, movSubtotal: number, movTax: number, movTotal: number, movLine: number,) {
        this.invoice = invoice;
        this.clCode = clCode;
        this.proSKU = proSKU;
        this.proQty = proQty;
        this.movPU = movPU;
        this.movNet = movNet;
        this.movDisc = movDisc;
        this.movSubtotal = movSubtotal;
        this.movTax = movTax;
        this.movTotal = movTotal;
        this.movLine = movLine;
    }
}
