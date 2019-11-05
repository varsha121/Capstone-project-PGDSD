package org.upgrad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upgrad.models.Item;
import org.upgrad.repositories.ItemRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Override
    public Item getItemById(int id) {
        return itemRepository.getItemById(id);
    }

    @Override
    public List<Item> getItemByPopularity(int restaurantId) {
        Iterable<Integer> itemIds = itemRepository.getItems(restaurantId);
        List<Item> itemList = new ArrayList();

        for (Integer id : itemIds) {
            Item item = itemRepository.getItemById(id);
            itemList.add(item);
        }
        return itemList;

    }
}
