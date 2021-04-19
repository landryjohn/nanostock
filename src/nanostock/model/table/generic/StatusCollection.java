package nanostock.model.table.generic;

public enum StatusCollection {
    PAIDSTATUS("payée") ,
    UNPAIDSTATUS("non payée") ,
    DELEVERED("livrée") ,
    UNDELIVERED("non livrée") ;

    private String statusValue ;

    StatusCollection( String statusValue ){
        this.statusValue = statusValue ;
    }

    public String toString(){
        return this.statusValue ;
    }

}
