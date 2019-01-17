package com.elegion.courserafirstcourseprogrammingtest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class CharacterCreator extends Observable  implements Serializable{

    public enum Specialization {
        WARRIOR, ARCHER, MAGE
    }

    public enum Race {
        HUMAN, ELF, ORC, DWARF
    }

    public enum Attribute {
        STRENGTH, AGILITY, INTELLECT, STAMINA, LUCK
    }

    public enum Perk {
        BERSERK, CALM, LIGHTWEIGHT, HEAVYARMORED, OBSERVANT, MEDITATIONS
    }

    private String mName;
    private Specialization mSpecialization;
    private Race mRace;
    private int mAvailablePoints;
    private FormatHelper mFormatHelper = new FormatHelper();

    private Map<String, Integer> mAttributesMap = new HashMap<>();
    private Map<String, Boolean> mPerksMap = new HashMap<>();


    public CharacterCreator() {
        mRace = Race.HUMAN;
        mSpecialization = Specialization.WARRIOR;
        mAvailablePoints = 5;
        mAttributesMap.put(Attribute.STRENGTH.name(), 5);
        mAttributesMap.put(Attribute.AGILITY.name(), 5);
        mAttributesMap.put(Attribute.INTELLECT.name(), 5);
        mAttributesMap.put(Attribute.STAMINA.name(), 5);
        mAttributesMap.put(Attribute.LUCK.name(), 5);
    }


    public String[] getSpecializations() {
        return getStrWithFirstUpperArray(Specialization.values());
    }

    public void setSpecialization(int position) {
        mSpecialization = getItem(position, Specialization.values());
    }

    public String[] getRaces() {
        return getStrWithFirstUpperArray(Race.values());
    }

    public void setRace(int position) {
        mRace = getItem(position, Race.values());
    }

    public String[] getAttributes() {
        return getStrWithFirstUpperArray(Attribute.values());
    }

    public String[] getPerks() {
        return getStrWithFirstUpperArray(Perk.values());
    }
    public void updateAttributeValue(int position, int updateTo) {
        Attribute[] values = Attribute.values();

        if (position < 0 || position >= values.length) {
            return;
        }

        String attribute = values[position].toString();

        if (!mAttributesMap.containsKey(attribute)) {
            mAttributesMap.put(attribute, 1);
        }

        if (updateTo < 0 && (mAttributesMap.get(attribute) + updateTo) > 0) {
            mAttributesMap.put(attribute, (mAttributesMap.get(attribute) + updateTo));
            mAvailablePoints += Math.abs(updateTo);
            setChanged();
            notifyObservers();
            return;
        }

        if (updateTo > 0 && mAvailablePoints >= updateTo) {
            mAttributesMap.put(attribute, (mAttributesMap.get(attribute) + updateTo));
            mAvailablePoints -= updateTo;
            setChanged();
            notifyObservers();
            return;
        }

        // TODO: 11.12.2017
        /*
        *  этот метод увеличивает/уменьшает соответствующее значение атрибута
        *  рекомендуется реализовывать его в последнюю очередь
        *
        * 1. на вход подается позиция изменяемого атрибута и на сколько нужно этот атрибут увеличить/уменьшить
        * 2. по позиции атрибута выявляется название атрибута из enum Attribute
        * 3. по названию атрибута получается значение атрибута из mAttributesMap
        * 4. если атрибут собирается увеличиться и у персонажа достаточно очков для увеличения атрибута (mAvailablePoints)
        *    или
        *    если атрибут собирается уменьшиться и уменьшенный атрибут будет больше 0,
        *    то атрибут изменяется, количество доступных очков меняется в противоположную сторону.
        *
        * 5. убедитесь в том, что измененное значение атрибута записано в mAttributesMap
        * 6. убедитесь в том, что измененное значение количества очков записано в mAvailablePoints;
        * 7. после изменения нужно вызвать методы setChanged(); notifyObservers();
        *    для того, чтобы изменения отразились на верстке
        *
        * пример работы алгоритма.
        * на вход подается (0, -1)
        * из значения 0 выясняем, что меняться будет атрибут STRENGTH
        * получаем текущее значение этого атрибута из mAttributesMap
        * допустим, оно равно 3
        * по условию все алгоритма все проходит
        * сила уменьшается до 2, количество доступных очков увеличивается на +1
        *
        * если STRENGTH равно 1, то ничего не происходит,
        * так как мы не можем уменьшить атрибут ниже 1
        *
        * если на вход пришло (0, 1)
        *   если доступных очков больше 0,
        *       то STRENGTH увеличивается на 1, количество доступных очков уменьшается на 1
        *   если количество доступных очков равно 0
        *       то мы не можем увеличить атрибут, ничего не происходит        *
        * */

    }

    protected <T> String[] getStrWithFirstUpperArray(T[] array) {
        String[] values = new String[array.length];

        for (int i = 0; i < array.length; i++) {
            values[i] = mFormatHelper.toStrWithFirstUpper(array[i].toString());
        }

        return values;
    }

    protected <T> T getItem(int position, T[] array) {
        if (position < 0) {
            return array[0];
        }

        if (position >= array.length) {
            return array[array.length - 1];
        }

        return array[position];
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAvailablePoints() {
        return String.valueOf(mAvailablePoints);
    }

    public Map<String, Integer> getAttributesMap() {
        return mAttributesMap;
    }

    public void checkPerk(String text, boolean isChecked) {
        mPerksMap.put(text, isChecked);
    }

    public Character create() {
        Character character = new Character();
        character.setName(mName);
        character.setRace(mRace);
        character.setSpecialization(mSpecialization);
        character.setAttributes(mAttributesMap);
        character.setPerks(mPerksMap);
        character.calculateParameters();
        return character;
    }

    public Specialization getSpecialization() {
        return mSpecialization;
    }

    public Race getRace() {
        return mRace;
    }

    public Map<String, Boolean> getPerksMap() {
        return mPerksMap;
    }

    public void setAvailablePoints(int availablePoints) {
        mAvailablePoints = availablePoints;
    }

    public int getRacePosition() {
        return mRace.ordinal();
    }

    public int getSpecializationPosition() {
        return mSpecialization.ordinal();
    }
}
