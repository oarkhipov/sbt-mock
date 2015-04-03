package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-vostrikov-mi on 03.04.2015.
 */
@XStreamAlias("namespace-aliases")
@ToString
public class NamespaceAliases {

    @XStreamImplicit(itemFieldName = "namespace")
    @Getter
    @Setter
    private List<NamespaceAliase> listOfNamespaces = new ArrayList<NamespaceAliase>();

    /**
     * конструктор
     */
    public NamespaceAliases() {
        listOfNamespaces = new ArrayList<NamespaceAliase>();
    }

    /**
     * получаем сверху алиас и добавлем его к существуюущим. При этом если такой алиас у нас уже есть - берем тот, что у нас уже был, пришедший игнорируем.
     * игнорировать нужно потому, что нам приходят алиасы сверху иерархии и мы их перезаприсываем
     *
     * @param nsNew
     */
    public void addNamespaces(NamespaceAliase nsNew) {
        if (!contains(nsNew.getAlias())) {
            listOfNamespaces.add(nsNew);
        }
    }

    /**
     * проверка, существует ли нужный нам alias в списске алиасов
     * @param alias
     * @return
     */
    public Boolean contains(String alias) {
        for (NamespaceAliase ns : getListOfNamespaces()) {
            if (ns.getAlias() == alias) {
                return true;
            }
        }
        return false;
    }


}

