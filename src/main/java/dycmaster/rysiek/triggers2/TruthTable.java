package dycmaster.rysiek.triggers2;


import dycmaster.rysiek.shared.Create;

import java.util.Arrays;
import java.util.Map;

public class TruthTable {

    private final String [] header;
    private final  Map<TruthTableRow, Boolean> table = Create.newMap();

    public TruthTable(String [] header) {
        this.header = header;
    }

    public void addRow(Boolean[] values, boolean output){
        TruthTableRow rowToAdd = new TruthTableRow(values);
        table.put(rowToAdd, output);
    }

    public boolean findOutputForValues(Boolean [] values){
        TruthTableRow rowToFind = new TruthTableRow(values);
        return table.get(rowToFind);
    }

    public String [] getHeader() {
        return header;
    }


    class TruthTableRow{


        private final  Boolean [] values;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TruthTableRow)) return false;

            TruthTableRow that = (TruthTableRow) o;

            return Arrays.equals(values, that.values);

        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(values);
        }

        TruthTableRow(Boolean[] values) {
            this.values = values;
        }
    }

}
