package ku.cs.duckdealer.services;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

public class PrintService {

    public void print(Node node, double width, double height) {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob();
        job.setPrinter(printer);
        double scaleX;
        double scaleY;
        if (width == -1 && height == -1) {
            return;
        } else if (height == -1) {
            scaleX = width / node.getBoundsInParent().getWidth();
            scaleY = scaleX;
        } else if (width == -1) {
            scaleY = height / node.getBoundsInParent().getHeight();
            scaleX = scaleY;
        } else {
            scaleX = width / node.getBoundsInParent().getWidth();
            scaleY = height / node.getBoundsInParent().getHeight();
        }
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        } else {
            System.out.println("Can't find printer.");
        }
        node.getTransforms().remove(scale);
    }

    public void print(Node node, Paper paper) {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob();
        job.setPrinter(printer);
        PageLayout pageLayout = printer.createPageLayout(paper, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = scaleX;
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        } else {
            System.out.println("Can't find printer.");
        }
        node.getTransforms().remove(scale);
    }

}
