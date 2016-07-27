package com.tradeengine;

import com.tradeengine.ProfileReader.entities.Address;
import com.tradeengine.ProfileReader.entities.CreditCard;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.time.LocalDate;

public class MyMatchers
{
    private MyMatchers()
    {
    }

    public static Matcher<LocalDate> sameAs(final LocalDate expectedLocalDate)
    {
        return new BaseMatcher<LocalDate>()
        {
            @Override
            public boolean matches(Object object)
            {
                LocalDate localDate = (LocalDate) object;
                if (localDate.isEqual(expectedLocalDate))
                    return true;
                else
                    return false;
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("LocalDates aren't same: ").appendValue(expectedLocalDate.toString());
            }
        };
    }

    public static Matcher<Address> sameAs(final Address expectedAddress)
    {
        return new BaseMatcher<Address>()
        {
            @Override
            public boolean matches(Object object)
            {
                Address address = (Address) object;
                if (!StringUtils.equals(address.getCountry(), expectedAddress.getCountry()))
                    return false;

                if (!StringUtils.equals(address.getCity(), expectedAddress.getCity()))
                    return false;

                if (!StringUtils.equals(address.getStreet(), expectedAddress.getStreet()))
                    return false;

                if (!StringUtils.equals(address.getZipCode(), expectedAddress.getZipCode()))
                    return false;

                return true;
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("addresses aren't same").appendValue("");
            }
        };
    }

    public static Matcher<CreditCard> sameAs(final CreditCard expectedCreditCard)
    {
        return new BaseMatcher<CreditCard>()
        {
            @Override
            public boolean matches(Object object)
            {
                final CreditCard creditCard = (CreditCard) object;

                if (!StringUtils.equals(creditCard.getNumber(), expectedCreditCard.getNumber()))
                    return false;
                if (creditCard.getBalance() != expectedCreditCard.getBalance())
                    return false;
                if (!StringUtils.equals(creditCard.getCurrency(), expectedCreditCard.getCurrency()))
                    return false;

                return true;
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("credit cards aren't same: ").appendValue(expectedCreditCard.toString());
            }
        };
    }

    public static Matcher<Customer> sameAs(final Customer expectedCustomer)
    {
        return new BaseMatcher<Customer>()
        {
            @Override
            public boolean matches(final Object object)
            {
                final Customer customer = (Customer) object;

                if (!StringUtils.equals(customer.getFirstname(), expectedCustomer.getFirstname()))
                    return false;
                if (!StringUtils.equals(customer.getLastname(), expectedCustomer.getLastname()))
                    return false;

                if (!sameAs(customer.getAddress()).matches(expectedCustomer.getAddress()))
                    return false;
                if (!sameAs(customer.getCreditCard()).matches(expectedCustomer.getCreditCard()))
                    return false;

                if (customer.getPoints() != null && expectedCustomer.getPoints() != null && !customer.getPoints().equals(expectedCustomer.getPoints()))
                    return false;
                if (customer.getTierLevel() != null && expectedCustomer.getTierLevel() != null && !customer.getTierLevel().equals(expectedCustomer.getTierLevel()))
                    return false;

                return true;
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("customers aren't same: ").appendValue(expectedCustomer.toString());
            }
        };
    }

    public static Matcher<CompletedOrder> sameAs(final CompletedOrder expectedCompletedOrder)
    {
        return new BaseMatcher<CompletedOrder>()
        {
            @Override
            public boolean matches(final Object object)
            {
                final CompletedOrder completedOrder = (CompletedOrder) object;
                if (!completedOrder.getTimeOfSale().isEqual(expectedCompletedOrder.getTimeOfSale()))
                    return false;

//                if (order.getProductIdList().size() != expectedOrder.getProductIdList().size() || expectedOrder.getProductIdList().containsAll(order.getProductIdList()))
//                    return false;

                if (completedOrder.getPrice().getAmount() != expectedCompletedOrder.getPrice().getAmount())
                    return false;

                if (completedOrder.getPrice().getTax() != expectedCompletedOrder.getPrice().getTax())
                    return false;

                if (completedOrder.getPrice().getPrice() != expectedCompletedOrder.getPrice().getPrice())
                    return false;

                return true;
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("orders aren't same: ").appendValue(expectedCompletedOrder.toString());
            }
        };
    }

    public static Matcher<ShoppingHistory> sameAs(final ShoppingHistory expectedShoppingHistory)
    {
        return new BaseMatcher<ShoppingHistory>()
        {
            @Override
            public boolean matches(final Object object)
            {
                final ShoppingHistory shoppingHistory = (ShoppingHistory) object;

                if (shoppingHistory.getCustomerId() != expectedShoppingHistory.getCustomerId())
                    return false;

//                if (!checkOrderListsAreSame(shoppingHistory))
//                    return false;

                if (shoppingHistory.getTotalAmount() != expectedShoppingHistory.getTotalAmount())
                    return false;

                if (shoppingHistory.getTotalTaxes() != expectedShoppingHistory.getTotalTaxes())
                    return false;

                return true;
            }

            private boolean checkOrderListsAreSame(ShoppingHistory shoppingHistory)
            {
                if (shoppingHistory.getCompletedOrderList().size() == expectedShoppingHistory.getCompletedOrderList().size() && isOrderListsSame(shoppingHistory))
                    return true;
                else
                    return false;
            }

            private boolean isOrderListsSame(ShoppingHistory shoppingHistory)
            {
                for (int i = 0; i < shoppingHistory.getCompletedOrderList().size(); i++)
                    if (!sameAs(shoppingHistory.getCompletedOrderList().get(i)).matches(expectedShoppingHistory.getCompletedOrderList().get(i)))
                        return false;
                return true;
            }

            @Override
            public void describeTo(final Description description)
            {
                description.appendText("shopping histories aren't same: ").appendValue(expectedShoppingHistory.toString());
            }
        };
    }
}
