package com.tallervehiculos.uth.views.ordendereparación;

import com.tallervehiculos.uth.data.entity.Orden_reparacion;
import com.tallervehiculos.uth.data.service.Orden_reparacionService;
import com.tallervehiculos.uth.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Orden de Reparación")
@Route(value = "orden-reparacion/:orden_reparacionID?/:action?(edit)", layout = MainLayout.class)
public class OrdendeReparaciónView extends Div implements BeforeEnterObserver {

    private final String ORDEN_REPARACION_ID = "orden_reparacionID";
    private final String ORDEN_REPARACION_EDIT_ROUTE_TEMPLATE = "orden-reparacion/%s/edit";

    private final Grid<Orden_reparacion> grid = new Grid<>(Orden_reparacion.class, false);

    private TextField id_orden;
    private TextField vehiculo_id;
    private TextField descripcion_problema;
    private TextField estado_reparacion;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Orden_reparacion> binder;

    private Orden_reparacion orden_reparacion;

    private final Orden_reparacionService orden_reparacionService;

    public OrdendeReparaciónView(Orden_reparacionService orden_reparacionService) {
        this.orden_reparacionService = orden_reparacionService;
        addClassNames("ordende-reparación-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("id_orden").setAutoWidth(true);
        grid.addColumn("vehiculo_id").setAutoWidth(true);
        grid.addColumn("descripcion_problema").setAutoWidth(true);
        grid.addColumn("estado_reparacion").setAutoWidth(true);
        grid.setItems(query -> orden_reparacionService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ORDEN_REPARACION_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(OrdendeReparaciónView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Orden_reparacion.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_orden).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_orden");
        binder.forField(vehiculo_id).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("vehiculo_id");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.orden_reparacion == null) {
                    this.orden_reparacion = new Orden_reparacion();
                }
                binder.writeBean(this.orden_reparacion);
                orden_reparacionService.update(this.orden_reparacion);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(OrdendeReparaciónView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> orden_reparacionId = event.getRouteParameters().get(ORDEN_REPARACION_ID).map(Long::parseLong);
        if (orden_reparacionId.isPresent()) {
            Optional<Orden_reparacion> orden_reparacionFromBackend = orden_reparacionService
                    .get(orden_reparacionId.get());
            if (orden_reparacionFromBackend.isPresent()) {
                populateForm(orden_reparacionFromBackend.get());
            } else {
                Notification.show(String.format("The requested orden_reparacion was not found, ID = %s",
                        orden_reparacionId.get()), 3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(OrdendeReparaciónView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        id_orden = new TextField("Id_orden");
        vehiculo_id = new TextField("Vehiculo_id");
        descripcion_problema = new TextField("Descripcion_problema");
        estado_reparacion = new TextField("Estado_reparacion");
        formLayout.add(id_orden, vehiculo_id, descripcion_problema, estado_reparacion);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Orden_reparacion value) {
        this.orden_reparacion = value;
        binder.readBean(this.orden_reparacion);

    }
}
