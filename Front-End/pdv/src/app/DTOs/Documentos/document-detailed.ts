import { Mov } from "./mov";

export class DocumentDetailed {
    invoice: number;
    docDate: Date;
    clCode: string;
    clName: string;
    movs: Mov[];
    docProductQty: number;
    docNet: number;
    docDiscount: number;
    docSubtotal: number;
    docTax: number;
    docTotal: number;
    docStatus: boolean;
    docCompleted: boolean;
    docType: number;

    constructor(invoice: number, docDate: Date, clCode: string, clName: string, movs: Array<Mov>, docProductQty: number, 
        docNet: number, docDiscount: number, docSubtotal: number, docTax: number, docTotal: number, docStatus: boolean, 
        docCompleted: boolean, docType: number,) {
        this.invoice = invoice;
        this.docDate = docDate;
        this.clCode = clCode;
        this.clName = clName;
        this.movs = movs;
        this.docProductQty = docProductQty;
        this.docNet = docNet;
        this.docDiscount = docDiscount;
        this.docSubtotal = docSubtotal;
        this.docTax = docTax;
        this.docTotal = docTotal;
        this.docStatus = docStatus;
        this.docCompleted = docCompleted;
        this.docType = docType;
    }
}
