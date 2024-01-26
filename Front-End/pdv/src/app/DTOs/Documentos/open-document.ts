export class OpenDocument {
    type: Number;
    newDoc: boolean;
    invoice: number;

    constructor(type: number, newDoc: boolean, invoice: number){
        this.type = type;
        this.newDoc = newDoc;
        this.invoice = invoice;
    }
}
