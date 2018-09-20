package ru.nik66.tracker.start;

import ru.nik66.tracker.models.Item;

import java.util.ArrayList;
import java.util.List;

class EditItem extends BaseAction {

    protected EditItem(int key, String name) {
        super(key, name);
    }

    @Override
    public void execute(Input input, Tracker tracker) {
        System.out.println("~~~~~ Edit Item ~~~~~");
        Item item = tracker.findById(input.ask("Enter item id: "));
        if (item == null) {
            System.out.println("~~~~~ Item not found! ~~~~~");
        } else {
            item.setName(input.ask("Enter item name:"));
            item.setDescription(input.ask("Enter item description: "));
            tracker.replace(item);
        }
    }

}

public class MenuTracker {

    private static final int ADD = 0;
    private static final int ALL = 1;
    public static final int EDIT = 2;
    private static final int DELETE = 3;
    private static final int ID = 4;
    private static final int NAME = 5;
    public static final int EXIT = 6;

    private Input input;
    private Tracker tracker;
    private List<UserAction> actions = new ArrayList<>();

    public MenuTracker(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    public void init() {
        // inner nonstatic class
        this.actions.add(this.new AddItem(ADD, "Add new item."));
        // inner static class
        this.actions.add(new MenuTracker.ShowItems(ALL, "Show all items."));
        // outer class
        this.actions.add(new EditItem(EDIT, "Edit items."));
        // anonymous class
        this.actions.add(new BaseAction(DELETE, "Delete item.") {
            @Override
            public void execute(Input input, Tracker tracker) {
                System.out.println("~~~~~ Delete Item ~~~~~");
                if (!tracker.delete(input.ask("Enter item id: "))) {
                    System.out.println("~~~~~ Item not found! ~~~~~");
                }
            }
        });
        this.actions.add(this.new FindById(ID, "Find item by id."));
        this.actions.add(this.new FindByName(NAME, "Find items by name."));
        this.actions.add(this.new Exit(EXIT, "Exit"));
    }

    public void select(int key) {
        this.actions.get(key).execute(this.input, this.tracker);
    }

    public void show() {
        System.out.println("~~~~~ MenuTracker ~~~~~");
        for (UserAction action : this.actions) {
            if (action != null) {
                System.out.println(action.info());
            }
        }
    }

    public List<UserAction> getActions() {
        return this.actions;
    }

    private class AddItem extends BaseAction {

        protected AddItem(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("~~~~~ Add New Item ~~~~~");
            String name = input.ask("Enter item name: ");
            String description = input.ask("Enter item description: ");
            Item item = new Item(name, description, 1L);
            tracker.add(item);
            System.out.println("~~~~~ New item id: " + item.getId() + " ~~~~~");
        }

    }

    private static class ShowItems extends BaseAction {

        protected ShowItems(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("~~~~~ Show All Items ~~~~~");
            for (Item item : tracker.findAll()) {
                System.out.printf("id: %s\tname: %s\tdescription: %s\tdate: %s%s",
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getCreate(),
                        System.lineSeparator());
                System.out.println("=================================================");
            }
        }

    }

    private class FindById extends BaseAction {

        protected FindById(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("~~~~~ Find Item By Id ~~~~~");
            Item item = tracker.findById(input.ask("Enter item id: "));
            if (item == null) {
                System.out.println("~~~~~ Item not found! ~~~~~");
            } else {
                System.out.printf("id: %s\tname: %s\tdescription: %s\tdate: %s%s",
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getCreate(),
                        System.lineSeparator());
                System.out.println("=================================================");
            }
        }

    }

    private class FindByName extends BaseAction {

        protected FindByName(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
            System.out.println("~~~~~ Find Item By Name ~~~~~");
            List<Item> items = tracker.findByName(input.ask("Enter item name: "));
            if (items.size() == 0) {
                System.out.println("~~~~~ Items not found! ~~~~~");
            } else {
                for (Item item : items) {
                    System.out.printf("id: %s\tname: %s\tdescription: %s\tdate: %s%s",
                            item.getId(),
                            item.getName(),
                            item.getDescription(),
                            item.getCreate(),
                            System.lineSeparator());
                    System.out.println("=================================================");
                }
            }
        }

    }

    private class Exit extends BaseAction {

        protected Exit(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, Tracker tracker) {
        }

    }

}
