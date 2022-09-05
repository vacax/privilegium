package edu.ucjc.privilegium.views.dashboard;


import com.storedobject.chart.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.ucjc.privilegium.views.MainLayout;

import javax.annotation.security.PermitAll;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@PermitAll
public class DashboardView extends Div {

    public DashboardView() {

        //setSpacing(false);

       /* Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);*/

        //add(new H2("Dashboard - TFM sobre PAM"));
        //add(new Paragraph("En desarrollo... 🤗"));
        graficos();


        setSizeFull();
        //setJustifyContentMode(JustifyContentMode.CENTER);
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        //getStyle().set("text-align", "center");
    }

    private void graficos(){
        // Creating a chart display area
        SOChart soChart = new SOChart();
        soChart.setSize("800px", "500px");

        // Let us define some inline data
        CategoryData labels = new CategoryData("Banana", "Apple", "Orange", "Grapes");
        Data data = new Data(25, 40, 20, 30);

        // We are going to create a couple of charts. So, each chart should be positioned appropriately
        // Create a self-positioning chart
        NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
        Position p = new Position();
        p.setTop(Size.percentage(50));
        nc.setPosition(p); // Position it leaving 50% space at the top

        // Second chart to add
        BarChart bc = new BarChart(labels, data);
        RectangularCoordinate coordinate =
                new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        p = new Position();
        p.setBottom(Size.percentage(55));
        coordinate.setPosition(p); // Position it leaving 55% space at the bottom
        bc.plotOn(coordinate); // Bar chart needs to be plotted on a coordinate system

        // Just to demonstrate it, we are creating a "Download" and a "Zoom" toolbox button
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());

        // Let's add some titles
        Title title = new Title("My First Chart");
        title.setSubtext("2nd Line of the Title");

        // Add the chart components to the chart display area
        soChart.add(nc, bc, toolbox, title);

        // Set the component for the view
        add(soChart);

    }


}
