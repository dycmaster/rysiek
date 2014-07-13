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

    public void addRow(boolean [] values, boolean output){
        TruthTableRow rowToAdd = new TruthTableRow(values);
        table.put(rowToAdd, output);
    }

    public boolean findOutputForValues(boolean [] values){
        TruthTableRow rowToFind = new TruthTableRow(values);
        return table.get(rowToFind);
    }

    public String [] getHeader() {
        return header;
    }

    class TruthTableRow{
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

        private final  boolean [] values;
        TruthTableRow(boolean[] values) {
            this.values = values;
        }
    }

}
