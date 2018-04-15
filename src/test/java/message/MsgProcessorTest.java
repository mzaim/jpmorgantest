package message;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class MsgProcessorTest {

    static MsgProcessor m;

    @BeforeAll
    public static void init(){
        m = new MsgProcessor();
    }

    @Test
    @Disabled
    public void testReportAdjustment(){
        Adjustment a1 = new Adjustment(OperationType.ADD, "apple", 20);
        Adjustment a2 = new Adjustment(OperationType.MULTIPLY, "pear", 2);
        List<Adjustment> adj = new ArrayList<>();
        adj.add(a1);
        adj.add(a2);
        m.setAdjustments(adj);

        m.reportAdjustments();
    }

    @Test
    public void testReportSalesSummary(){
        List<Integer> appleSales = new ArrayList<>();
        appleSales.add(0, 100);
        appleSales.add(1, 2000);
        List<Integer> pearSales  = new ArrayList<>();
        pearSales.add(0, 50);
        pearSales.add(1, 5000);
        Map<String, List<Integer>> salesPerProd = new HashMap<>();
        salesPerProd.put("apple", appleSales);
        salesPerProd.put("pear", pearSales);
        m.setSalesPerProduct(salesPerProd);

        m.reportSalesSummary();
    }

    @Test
    public void testCreateMultiple(){
        String message = "20 sales of apples at 10p each";

        Sale actual = m.createMultiple(message);

        Sale expected = new Sale("apples", 10, 20);
        assertThat(actual, is(expected));
    }

    @Test
    public void testCreateSingle(){
        String message = "apple at 10p";

        Sale actual = m.createSingle(message);

        Sale expected = new Sale("apple", 10);
        assertThat(actual, is(expected));
    }

    @Test
    public void testAdjustSales(){
        List<Sale> s = new ArrayList<>();
        s.add(0, new Sale("apple", 2, 50));
        s.add(1, new Sale("apple", 5));
        s.add(2, new Sale("pear", 100, 5));
        m.setSales(s);
        Adjustment a = new Adjustment(OperationType.SUBSTRACT, "apple", 1);

        m.adjustSales(a);

        List<Sale> expected = new ArrayList<>();
        expected.add(0, new Sale("apple", 1, 50)); //modify the apple sales
        expected.add(1, new Sale("apple", 4));
        expected.add(2, new Sale("pear", 100, 5)); //don't touch the pear sale
        List<Sale> actual = m.getSales();
        assertThat(actual.get(0), is(expected.get(0)));
        assertThat(actual.get(1), is(expected.get(1)));
        assertThat(actual.get(2), is(expected.get(2)));
    }

    @Test
    public void testAddSale(){
        Sale s = new Sale("apple", 10);

        m.addSale(s);

        List actualSales = m.getSales();
        assertThat(actualSales.size(), is(1));
        assertThat(actualSales.get(0), is(s));
    }

    @Test
    public void testUpdateSalesPerProd(){
        List<Integer> appleSales = new ArrayList<>();
        appleSales.add(0, 100);
        appleSales.add(1, 2000);
        List<Integer> pearSales  = new ArrayList<>();
        pearSales.add(0, 50);
        pearSales.add(1, 5000);
        Map<String, List<Integer>> salesPerProd = new HashMap<>();
        salesPerProd.put("apple", appleSales);
        salesPerProd.put("pear", pearSales);
        m.setSalesPerProduct(salesPerProd);

        Sale s = new Sale("apple", 10);
        m.updateSalesPerProd(s);

        Map<String, List<Integer>> actual = m.getSalesPerProduct();
        salesPerProd.get("apple").set(0, 101);
        Map<String, List<Integer>> expected = new HashMap<>(salesPerProd);
        expected.get("apple").set(0, 101);
        expected.get("apple").set(1, 2010);

        assertThat(actual, is(expected));
    }
}
