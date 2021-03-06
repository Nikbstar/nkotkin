package ru.nik66.tracker.start;

import ru.nik66.tracker.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Tracker implements ITracker {

    private List<Item> items = new ArrayList<>();

    @Override
    public void add(Item item) {
        this.items.add(item);
    }

    @Override
    public void replace(Item item) {
        for (int iterator = 0; iterator < this.items.size(); iterator++) {
            if (this.items.get(iterator).getId().equals(item.getId())) {
                this.items.set(iterator, item);
                break;
            }
        }
    }

    @Override
    public boolean delete(String id) {
        boolean result = false;
        for (Item item : this.items) {
            if (item.getId().equals(id)) {
                result = this.items.remove(item);
                break;

            }
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        return this.items;
    }

    @Override
    public List<Item> findByName(final String name) {
        Predicate<Item> search = item -> item.getName().contains(name);
        return this.items.stream().filter(search).collect(Collectors.toList());
    }

    @Override
    public Item findById(final String id) {
        final Predicate<Item> search = item -> item.getId().equals(id);
        Optional<Item> item = this.items.stream().filter(search).findFirst();
        return item.orElse(null);
    }

}
