package lk.vega.charger.centralservice.client.web.domain;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/8/15.
 * Time on 2:39 PM
 */
public class GeneralProperty extends DomainBeanImpl
{
    private String name;
    private boolean selected;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected( boolean selected )
    {
        this.selected = selected;
    }

    @Override
    public void createBean( Object object )
    {
        setName( (String)object );
        setSelected( false );
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        super.decodeBeanToReal( object );
    }
}
