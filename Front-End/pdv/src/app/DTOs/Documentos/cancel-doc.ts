export class CancelDoc {
    invoice: number;
    clCode: string;

    constructor (invoice: number, clCode: string){
        this.invoice = invoice;
        this.clCode = clCode;
    }
}
