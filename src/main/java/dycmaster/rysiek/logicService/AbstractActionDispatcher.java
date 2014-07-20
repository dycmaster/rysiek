package dycmaster.rysiek.logicService;


import dycmaster.rysiek.shared.ConfigTools;
import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.shared.FileTools;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

public abstract class AbstractActionDispatcher implements IActionDispatcher {

    private Map<String, List<Pair<Boolean, String>>> dispatchingTable;
    private ILogicService logicService;

    public AbstractActionDispatcher(){
        this(ConfigFiles.ActionDispatcherTable.getUrl());
    }

    public AbstractActionDispatcher(URL dispatchingTableConfig){
        initDispatchingTable(dispatchingTableConfig);
    }

    private static Entry<String, List<Pair<Boolean, String>>> parseOneLineToDispatchingTableRow(String line) {
        String[] values = line.split(ConfigTools.separator);
        Boolean triggeringState = Integer.parseInt(values[1]) != 0;
        List<Pair<Boolean, String>> res = Create.newList();
        res.add(new Pair<Boolean, String>(triggeringState, values[2]));
        return new AbstractMap.SimpleEntry<String, List<Pair<Boolean, String>>>(values[0], res);
    }

    protected static Map<String, List<Pair<Boolean, String>>> readDispatchingTableFromFile(URL dispatchingTableFile) throws IOException {
        File actionDispatcherConfig = FileTools.openFile(dispatchingTableFile);
        return Files.lines(actionDispatcherConfig.toPath())
                .filter(ConfigTools::isActionDispatcherConfigLineToBeProcessed)
                .map(ConfigTools::getConfigLinePartToProcess)
                .map(AbstractActionDispatcher::parseOneLineToDispatchingTableRow)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (x, y) -> {
                    x.addAll(y);
                    return x;
                }));
    }

    public ILogicService getLogicService() {
        return logicService;
    }

    @Autowired
    @Required
    public void setLogicService(ILogicService logicService) {
        this.logicService = logicService;
    }

    protected List<String> findActionsToFire(String source, boolean state) {
        if (dispatchingTable != null && dispatchingTable.get(source)!=null) {
            return dispatchingTable.get(source).stream()
                    .filter(x -> x.getKey().equals(state))
                    .map(Pair::getValue)
                    .collect(Collectors.toList());
        } else {
            return Create.newList();
        }
    }

    protected void initDispatchingTable(URL dispatchingTableConfig) {
        try {
            dispatchingTable = readDispatchingTableFromFile(dispatchingTableConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
