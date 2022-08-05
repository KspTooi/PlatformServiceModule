package com.ksptooi.asf.extendsbuildin.service;

import com.google.inject.Inject;
import com.ksptooi.asf.commons.CommandLineTable;
import com.ksptooi.asf.core.entities.Document;
import com.ksptooi.asf.core.service.DocumentService;
import com.ksptooi.asf.extendsbuildin.enums.DocumentType;

import java.util.List;

public class AppLibraryService {

    @Inject
    private DocumentService documentService;

    public void showAppLibraryList(){

        List<Document> documentByType = documentService.getDocumentByType(DocumentType.APP_LIB.getName());

        CommandLineTable cliTable = new CommandLineTable();
        cliTable.setShowVerticalLines(true);

        cliTable.setHeaders("Name","Metadata");

        for(Document item : documentByType){
            cliTable.addRow(item.getName(),item.getMetadata());
        }

        cliTable.print();
    }


}
