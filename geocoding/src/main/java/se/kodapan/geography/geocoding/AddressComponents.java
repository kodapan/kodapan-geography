package se.kodapan.geography.geocoding;

import se.kodapan.collections.MapSet;

import java.io.Serializable;
import java.util.*;

/**
 * @author kalle
 * @since 2010-jul-20 11:36:09
 */
public class AddressComponents extends ArrayList<AddressComponent> implements Serializable {

  private static final long serialVersionUID = 1l;

  private MapSet<AddressComponentType, AddressComponent> typeIndex = new MapSet<AddressComponentType, AddressComponent>();

  public AddressComponents(int i) {
    super(i);
  }

  public AddressComponents() {
  }

  public void reconstructIndex() {
    MapSet<AddressComponentType, AddressComponent> typeIndex = new MapSet<AddressComponentType, AddressComponent>();
    for (AddressComponent component : this) {
      for (AddressComponentType type : component.getTypes()) {
        typeIndex.add(type, component);
      }
    }
    this.typeIndex = typeIndex;
  }

  @Override
  public boolean removeAll(Collection<?> objects) {
    boolean success = super.removeAll(objects);
    if (success) {
      for (Object object : objects) {
        removeFromIndex((AddressComponent)object);
      }
    }
    return success;
  }

  @Override
  protected void removeRange(int i, int i1) {
    reconstructIndex();
  }

  @Override
  public boolean addAll(Collection<? extends AddressComponent> addressComponents) {
    boolean success = super.addAll(addressComponents);
    if (success) {
      for (AddressComponent component : addressComponents) {
        indexComponent(component);
      }
    }
    return success;
  }

  @Override
  public boolean addAll(int i, Collection<? extends AddressComponent> addressComponents) {
    boolean success = super.addAll(i, addressComponents);
    if (success) {
      for (AddressComponent component : addressComponents) {
        indexComponent(component);
      }
    }
    return success;
  }

  @Override
  public AddressComponent remove(int i) {
    AddressComponent component = super.remove(i);
    if (component != null) {
      removeFromIndex(component);
    }
    return component;
  }

  @Override
  public boolean remove(Object o) {
    AddressComponent component = (AddressComponent) o;
    boolean success = super.remove(component);
    if (success) {
      removeFromIndex(component);
    }
    return success;
  }

  private void removeFromIndex(AddressComponent component) {
    for (AddressComponentType type : component.getTypes()) {
      typeIndex.get(type).remove(component);
    }
  }

  @Override
  public void clear() {
    super.clear();
    typeIndex.clear();
  }

  @Override
  public boolean add(AddressComponent component) {
    boolean success = super.add(component);
    if (component != null && success) {
      indexComponent(component);
    }
    return success;
  }

  private void indexComponent(AddressComponent component) {
    for (AddressComponentType type : component.getTypes()) {
      typeIndex.add(type, component);
    }
  }

  @Override
  public void add(int i, AddressComponent component) {
    super.add(i, component);
    if (component != null) {
      indexComponent(component);
    }
  }

  @Override
  public AddressComponent set(int i, AddressComponent component) {
    AddressComponent previous = super.set(i, component);
    if (previous != null) {
      typeIndex.removeSetValue(previous);
    }
    if (component != null) {
      indexComponent(component);
    }
    return previous;
  }

  public AddressComponent getUnique(AddressComponentType... types) {
    List<AddressComponent> list = list(types);
    if (list.size() == 0) {
      return null;
    } else if (list.size() == 1) {
      return list.get(0);
    } else {
      throw new NoSuchElementException("Expected 0 or 1 but found " + list.size() + " address componentes with type " + Arrays.asList(types));
    }
  }


  public AddressComponent get(AddressComponentType... types) {
    List<AddressComponent> list = list(types);
    if (list.size() != 1) {
      return null;
    } else {
      return list.get(0);
    }
  }

  public List<AddressComponent> list(AddressComponentType... types) {
    List<AddressComponent> list = new ArrayList<AddressComponent>();
    List<AddressComponentType> typesList = Arrays.asList(types);
    Set<AddressComponent> set = typeIndex.get(types[0]);
    if (set != null && set.size() > 0) {
      for (AddressComponent component : set) {
        if (component.getTypes().containsAll(typesList)) {
          list.add(component);
        }
      }
    }
    return list;
  }


}
