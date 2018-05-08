package ru.nik66.dom;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DepthOfMarkerTest {

    private DepthOfMarket dom;

    @Before
    public void init() {
        this.dom = new DepthOfMarket();
    }

    @Test
    public void whenHandleAddTypeOrderWithSamePriceThenVolumeIncrease() {
        Order order = new Order("GAZ", Type.ADD, Action.ASK, 11d, 10);
        assertThat(this.dom.orderHandler(order), is(true));
        assertThat(this.dom.getGlasses().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getBid().size(), is(0));
        assertThat(this.dom.getGlasses().get(0).getBook(), is("GAZ"));
        Order tmp = this.dom.getGlasses().get(0).getAsk().iterator().next();
        assertThat(tmp.getVolume(), is(10));
        Order order1 = new Order("GAZ", Type.ADD, Action.ASK, 11d, 10);
        assertThat(this.dom.orderHandler(order1), is(true));
        assertThat(this.dom.getGlasses().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getBid().size(), is(0));
        assertThat(tmp.getVolume(), is(20));
    }

    @Test
    public void whenHandleAddTypeOrderWithOtherPriceThenIncreaseGlassSize() {
        Order order = new Order("GAZ", Type.ADD, Action.ASK, 11d, 10);
        this.dom.orderHandler(order);
        Order order1 = new Order("GAZ", Type.ADD, Action.ASK, 10d, 10);
        assertThat(this.dom.orderHandler(order1), is(true));
        assertThat(this.dom.getGlasses().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(2));
        assertThat(this.dom.getGlasses().get(0).getBid().size(), is(0));
    }

    @Test
    public void whenHandleAddTypeOrdersWithDifferentBooksThenCreateNewGlass() {
        Order order = new Order("GAZ", Type.ADD, Action.ASK, 11d, 10);
        this.dom.orderHandler(order);
        Order order1 = new Order("LUK", Type.ADD, Action.BID, 10d, 10);
        assertThat(this.dom.orderHandler(order1), is(true));
        assertThat(this.dom.getGlasses().size(), is(2));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(1));
        assertThat(this.dom.getGlasses().get(1).getBid().size(), is(1));
    }

    @Test
    public void whenHandleDeleteTypeOrderThenItsDeleted() {
        Order order = new Order("GAZ", Type.ADD, Action.ASK, 11d, 10);
        this.dom.orderHandler(order);
        order.setType(Type.DELETE);
        assertThat(this.dom.orderHandler(order), is(true));
        assertThat(this.dom.getGlasses().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(0));
        assertThat(this.dom.getGlasses().get(0).getBid().size(), is(0));
    }

    @Test
    public void whenAddOrdersThenTheySortByHighToLowPrice() {
        Order order1 = new Order("GAZ", Type.ADD, Action.ASK, 6d, 10);
        Order order2 = new Order("GAZ", Type.ADD, Action.ASK, 8d, 10);
        Order order3 = new Order("GAZ", Type.ADD, Action.ASK, 10d, 10);
        Order order4 = new Order("GAZ", Type.ADD, Action.ASK, 11d, 10);
        Order order5 = new Order("GAZ", Type.ADD, Action.ASK, 9d, 10);
        this.dom.orderHandler(order1);
        this.dom.orderHandler(order2);
        this.dom.orderHandler(order3);
        this.dom.orderHandler(order4);
        this.dom.orderHandler(order5);
        Iterator<Order> iterator = this.dom.getGlasses().get(0).getAsk().iterator();
        assertThat(iterator.next().getPrice(), is(11d));
        assertThat(iterator.next().getPrice(), is(10d));
        assertThat(iterator.next().getPrice(), is(9d));
        assertThat(iterator.next().getPrice(), is(8d));
        assertThat(iterator.next().getPrice(), is(6d));
    }

    @Test
    public void whenMergeOrders() {
        Order orderBid = new Order("GAZ", Type.ADD, Action.BID, 10d, 9);
        this.dom.orderHandler(orderBid);
        Order orderAsk = new Order("GAZ", Type.ADD, Action.ASK, 11d, 10);
        assertThat(this.dom.orderHandler(orderAsk), is(true));
        assertThat(this.dom.getGlasses().get(0).getBid().size(), is(0));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().iterator().next().getVolume(), is(1));
        Order addToAskVolume = new Order("GAZ", Type.ADD, Action.ASK, 11d, 5);
        assertThat(this.dom.orderHandler(addToAskVolume), is(true));
        assertThat(this.dom.getGlasses().get(0).getAsk().iterator().next().getVolume(), is(6));
        Order orderBidHighPrice = new Order("GAZ", Type.ADD, Action.BID, 12d, 9);
        assertThat(this.dom.orderHandler(orderBidHighPrice), is(true));
        assertThat(this.dom.getGlasses().get(0).getBid().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(1));
        Order orderBidLowPrice = new Order("GAZ", Type.ADD, Action.BID, 10d, 3);
        assertThat(this.dom.orderHandler(orderBidLowPrice), is(true));
        assertThat(this.dom.getGlasses().get(0).getBid().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().iterator().next().getVolume(), is(3));
        Order orderBidLowPrice1 = new Order("GAZ", Type.ADD, Action.BID, 10d, 3);
        assertThat(this.dom.orderHandler(orderBidLowPrice1), is(true));
        assertThat(this.dom.getGlasses().get(0).getBid().size(), is(1));
        assertThat(this.dom.getGlasses().get(0).getAsk().size(), is(0));
    }

    @Test
    public void whenPrintGlass() {
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.BID, 70d, 22));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 220d, 6));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 200d, 5));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.BID, 50d, 18));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 180d, 14));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.BID, 40d, 11));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 170d, 2));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.BID, 60d, 4));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 160d, 6));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.BID, 20d, 178));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 140d, 8));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 110d, 1));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.BID, 90d, 1));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 190d, 3));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.BID, 80d, 8));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.BID, 30d, 2));
        this.dom.orderHandler(new Order("GAZ", Type.ADD, Action.ASK, 150d, 13));
        for (String s : this.dom.getGlasses().get(0).toStringList()) {
            System.out.println(s);
        }
    }
}
