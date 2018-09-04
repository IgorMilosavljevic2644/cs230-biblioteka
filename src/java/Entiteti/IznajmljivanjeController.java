package Entiteti;

import Entiteti.util.JsfUtil;
import Entiteti.util.PaginationHelper;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import java.util.Date;

@Named("iznajmljivanjeController")
@SessionScoped
public class IznajmljivanjeController implements Serializable {

    private Iznajmljivanje current;
    private Integer currentKorisnikId;
    private Integer currentKnjigaId;
    private DataModel items = null;
    private DataModel knjigaItems = null;
    @EJB
    private Entiteti.IznajmljivanjeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public IznajmljivanjeController() {
    }

    public Iznajmljivanje getSelected() {
        if (current == null) {
            current = new Iznajmljivanje();
            selectedItemIndex = -1;
        }
        return current;
    }

    private IznajmljivanjeFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List?faces-redirect=true";
    }

    public String prepareView() {
        current = (Iznajmljivanje) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        current = new Iznajmljivanje();
        selectedItemIndex = -1;
        return "Create?faces-redirect=true";
    }
    
    public String prepareIzdavanje(){
        current = new Iznajmljivanje();
        selectedItemIndex = -1;
        return "Iznajmi?faces-redirect=true";
    }
    
    public String postaviKnjigu(Knjiga knjiga) {
        System.out.println("KNJIGA!!!!!!!!");
        System.out.println(current);
        current.setKnjigaId(knjiga);
        currentKnjigaId = knjiga.getKnjigaId();
        System.out.println(current.getKnjigaId());
        return "Iznajmi";
    }
    
    public String postaviKorisnika(Korisnik korisnik) {
        current.setKorisnikId(korisnik);
        currentKorisnikId = korisnik.getKorisnikId();
        return "Iznajmi";
    }
    
    public String iznajmi() {
        current.setIznajmljivanjeDatum(new Date());

        getFacade().create(current);
        current = new Iznajmljivanje();
        currentKorisnikId = null;
        currentKnjigaId = null;
        selectedItemIndex = -1;
        JsfUtil.addSuccessMessage("Knjiga uspešno iznajmljena");
        return "Iznajmi";
    }
    
    public Integer getCurrentKnjigaId() {
        return this.currentKnjigaId;
    }
    
    public Integer getCurrentKorisnikId() {
        return this.currentKorisnikId;
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("IznajmljivanjeCreated"));
            current = new Iznajmljivanje();
            selectedItemIndex = -1;
            return "Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Iznajmljivanje) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit?faces-redirect=true";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("IznajmljivanjeUpdated"));
            return "Edit";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Iznajmljivanje) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("IznajmljivanjeDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Iznajmljivanje getIznajmljivanje(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Iznajmljivanje.class)
    public static class IznajmljivanjeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            IznajmljivanjeController controller = (IznajmljivanjeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "iznajmljivanjeController");
            return controller.getIznajmljivanje(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Iznajmljivanje) {
                Iznajmljivanje o = (Iznajmljivanje) object;
                return getStringKey(o.getIznajmljivanjeId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Iznajmljivanje.class.getName());
            }
        }

    }

}
