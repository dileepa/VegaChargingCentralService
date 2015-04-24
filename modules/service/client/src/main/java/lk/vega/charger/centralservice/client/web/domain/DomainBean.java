package lk.vega.charger.centralservice.client.web.domain;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/24/15.
 * Time on 10:21 AM
 */
public interface DomainBean
{
    /**
     *
     * This is the interface for create bean object.
     * in Child class must contain
     * override below methods
     * getters and setters
     * return List static method
     */

    public void createBean(Object object);

    public void decodeBeanToReal (Object object);

}
