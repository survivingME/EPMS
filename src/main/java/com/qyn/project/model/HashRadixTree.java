/*
The MIT License

Copyright (c) 2011 David J Singer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.qyn.project.model;

import com.qyn.project.exception.DuplicateKeyException;
import com.qyn.project.model.RadixEntry;
import com.qyn.project.model.RadixTreeNode;
import com.qyn.project.model.RadixTreeSearchResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

/**
 * 哈希表基数树
 * @param <T>
 */
public class HashRadixTree<T> {
    RadixTreeNode<T> root;
    int size;
    HashMap<String,T> hashMap;

    public HashRadixTree() {
        root = new RadixTreeNode<T>();
        hashMap = new HashMap<>();
        root.key = "";
        root.real = false;
        size = 0;
    }
    RadixTreeSearchResult<T> searchKey(String key, RadixTreeNode<T> node, int tag){
//        System.out.println("22");
        RadixTreeNode<T> parent = null;
        RadixTreeSearchResult<T> result = new RadixTreeSearchResult<T>();
        LinkedList<RadixTreeNode<T>> pathNode = new LinkedList<>();
        if (node.key.isEmpty()){
            if (node.childrenSize == 0){
                result.lastMatchKey = "";
                return result;
            }
            parent = node;
            node = node.firstChild;
        }
//        System.out.println("first : " + node.key);
        String workKey = key;
        String matchedKey = "";
        RadixTreeNode<T> previousNode = null;
        int keyLength = workKey.length();
        String nodeKey = "";
        int nodeLength = 0;
        int maxLoops = 0;
        int numberOfMatchingCharacters = 0;
        boolean belongsBefore = false;
        do {
            nodeKey = node.key;
            nodeLength = nodeKey.length();
            maxLoops = (nodeKey.length() > workKey.length()) ? workKey.length() : nodeKey.length();
            for (numberOfMatchingCharacters = 0; numberOfMatchingCharacters < maxLoops ; numberOfMatchingCharacters++) {
                if (workKey.charAt(numberOfMatchingCharacters) != nodeKey.charAt(numberOfMatchingCharacters)){
                    if (workKey.charAt(numberOfMatchingCharacters) < nodeKey.charAt(numberOfMatchingCharacters))
                        belongsBefore = true;
                    break;
                }
            }
            if (numberOfMatchingCharacters == nodeLength ){  //该节点是要查询值的前缀
                matchedKey += nodeKey;
                if (tag == 1){
//                    System.out.println("mid : "+node.key+"  parent : "+parent.key);
//                    pathNode.addFirst(parent);
                    pathNode.addFirst(node);
                }

                if (node.real){  //如果该节点是添加的key
                    result.matchList.addFirst(node);
                    result.matchFullKeyList.addFirst(matchedKey);
                }
                if (keyLength == nodeLength){   // 该节点长度 == 要查询值的长度，则找到该值，跳出循环
                    result.exactMatch = true;
                    if (tag==1){
                        node.searchKey++;
                    }
                    break;
                }
                if (node.childrenSize == 0)   //此时，没有子节点，则匹配失败
                    break;
                if (tag==1){
                    node.searchKey++;
                }
                // 表示此时还有孩子节点，进入孩子节点
                parent = node;
                previousNode = null;
                node = node.firstChild;
                workKey = workKey.substring(nodeLength);
                keyLength = keyLength - nodeLength;
                continue;
            } else if (numberOfMatchingCharacters > 0){  //不是前缀，只是匹配了部分字符，则匹配失败
                result.partialMatch = node;
                break;
            }
            if ( belongsBefore )
                break;
            if (node.nextNode == null){
                break;
            } else {
                previousNode = node;
                node = node.nextNode;
                continue;
            }
        } while ( ! belongsBefore );
        result.lastTry = node;
        result.lastTryNumMatchingChars = numberOfMatchingCharacters;
        result.parent = parent;
        result.previousNode = previousNode;
        result.lastMatchKey = matchedKey;
        result.belongsBefore = belongsBefore;

        //hash表基数树操作，K=500
        if (tag == 1){
            for (int i=0;i+1<pathNode.size();i++) {
                RadixTreeNode<T> child = pathNode.get(i);
                RadixTreeNode<T> father = pathNode.get(i+1);
//                if (child.childrenSize==0&&child.searchKey>=500){
                if (child.childrenSize==0&&child.searchKey>=500&&child.searchKey>=(father.searchKey/3)){
                    father.searchKey -= child.searchKey;
//                    hashTable.add(key,child.value);
                    hashMap.put(key,child.value);
                    father.childrenSize--;
                    this.remove(key);
                }
            }
        }


        return result;
    }
    /**
     * Find a value based on its corresponding key.
     *
     * @param key The key for which to search the tree.
     * @return The value corresponding to the key. null if it can not find the key
     */
    public RadixEntry<T> find(String key) {

        if (hashMap.containsKey(key)){
            return new RadixEntry<T>(key,hashMap.get(key));
        }
        RadixTreeSearchResult<T> result = searchKey(key, root, 1);
        if ( result.exactMatch && result.lastTry.real ) {
            return new RadixEntry<T>(result.matchFullKeyList.getFirst(), result.matchList.getFirst().value);
        }
        return null;
    }
    /**
     * Insert a new string key and its value to the tree.
     *
     * @param key
     *            The string key of the object
     * @param value
     *            The value that need to be stored corresponding to the given
     *            key.
     * @throws DuplicateKeyException
     */
    public void insert(String key, T value) throws DuplicateKeyException {
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        if ( ! continueInsert(key, value, result) )
            return;
        insert(key, value, result);
    }
    protected void insert(String key, T value, RadixTreeSearchResult<T> result ) throws DuplicateKeyException {
        RadixTreeNode<T> node;
        try {
            if (result.exactMatch){
                if (result.lastTry.real) {
                    // node already has a value. default action is to throw exception.
                    // Over ride below function for cusomt handeling.
                    //result.lastTry.value = dupHandler.duplicateKeyHandler(result.lastTry.value, value);
                    result.lastTry.value = dupKeyHandler(result.lastTry.value, value);
                    return;
                } else {
                    // found an empty home. Yea.  将空节点标记为节点
                    result.lastTry.value = value;
                    result.lastTry.real = true;
                }
            } else if (result.partialMatch != null) { //已有的树中没有完全匹配的节点
                // Split node with partial match, set node to it (comments will refer to the var name node)
                node = result.partialMatch;
                int numMatchingChars = result.lastTryNumMatchingChars;
                RadixTreeNode<T> n1 = new RadixTreeNode<T>();
                n1.key = node.key.substring(numMatchingChars);
                n1.real = node.real;
                n1.value = node.value;
                n1.firstChild = node.firstChild;
                n1.childrenSize = node.childrenSize;
                int matchedKeyLen = result.lastMatchKey.length();
                node.key = key.substring(matchedKeyLen, matchedKeyLen + numMatchingChars);
                if(numMatchingChars < (key.length() - matchedKeyLen)) {
                    // The new key is longer than the matched portion.
                    // So add a node, n2 as a child of node.
                    // n2 will be the new key and value.
                    RadixTreeNode<T> n2 = new RadixTreeNode<T>();
                    n2.key = key.substring(matchedKeyLen + numMatchingChars);
                    n2.real = true;
                    n2.value = value;
                    n2.childrenSize = 0;
                    node.real = false;
                    node.childrenSize = 2;
                    node.value = null;
                    if (result.belongsBefore){
                        // new key in n2 belongs before partial match found
                        node.firstChild = n2;
                        n2.nextNode = n1;
                        n1.preNode = n2;
                    } else {
                        node.firstChild = n1;
                        n1.nextNode = n2;
                        n2.preNode = n1;
                    }
                } else {
                    // the new key is the length of the matched characters.
                    // set the new value to this node.
                    node.value = value;
                    node.real = true;
                    node.firstChild = n1;
                    node.childrenSize = 1;
                }
            } else {
                // no match found. Add as new child.
                node = new RadixTreeNode<T>();
                node.key = key.substring(result.lastMatchKey.length());

                node.real = true;
                node.value = value;
                node.childrenSize = 0;
                if (result.parent == null){

                    root.firstChild = node;
                    root.childrenSize = 1;
                } else {

                    if (result.lastTryNumMatchingChars > 0){
                        result.lastTry.childrenSize = 1;
                        result.lastTry.firstChild = node;
                    } else {
                        RadixTreeNode<T> parent = result.parent;
                        parent.childrenSize++;
                        if (result.belongsBefore){
                            if (result.previousNode == null){
                                if (parent.childrenSize != 0 ){
                                    node.nextNode = parent.firstChild;
                                    parent.firstChild.preNode = node;
                                }
                                parent.firstChild = node;
                            } else {
                                node.nextNode = result.previousNode.nextNode;
                                result.previousNode.nextNode.preNode = node;
                                result.previousNode.nextNode = node;
                                node.preNode = result.previousNode;
                            }
                        } else{
                            result.lastTry.nextNode = node;
                            node.preNode = result.lastTry;
                        }
                    }
                }

            }
        } catch (DuplicateKeyException e) {
            // re-throw the exception with 'key' in the message
//            throw new DuplicateKeyException(key);
        }
        size++;
    }
    /**
     * Function is called if a duplicate key is encountered during an insert.
     * The builtin function throws DuplicateKeyException("Duplicate key"). To
     * Override to provide your own handling of duplicate keys.
     *
     * @param curValue  value for key already in the tree
     * @param newValue  value associated with new key trying to be inserted.
     * @return value to replace the current value associated with the key in the
     *         tree.
     */
    public T dupKeyHandler(T curValue, T newValue){
        throw new DuplicateKeyException("Duplicate key");
        //return curValue + "!!" + newValue;
    }
    /**
     * continueInsert when overridden can be used for things like trie reduction
     * by identifying they new value is "essentially" the same as the value for
     * a shorter prefix and we don't need to insert or redirects where to insert.
     * Here it is only a place holder that does nothing.
     * @param newVal: the new value for the proposed key/val.
     */
    public boolean continueInsert(String key, T newVal, RadixTreeSearchResult<T> searchResult ){
        return true;
    }
    /**
     * Delete a key and its associated value from the tree.
     * @param key The key of the node that need to be deleted
     * @return
     */
    public boolean delete(String key){
        // if remove succeds, (key found) remove returns the node and we return true.
        return (remove(key) != null);
    }
    public RadixTreeNode<T> remove(String key) {
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        if (result.exactMatch && result.lastTry.real)
            return remove(result.lastTry, result.previousNode, result.parent);
        else
            return null;
    }
    protected RadixTreeNode<T> remove(RadixTreeNode<T> node, RadixTreeNode<T> previous, RadixTreeNode<T> parent) {
        parent.searchKey -= node.searchKey;
        RadixTreeNode<T> children = node.firstChild;
        int childrenSize = node.childrenSize;
        if (childrenSize < 2){
            if (childrenSize == 0){
                if (parent.childrenSize == 1)
                    parent.firstChild = null;
                else if ( previous == null)
                    parent.firstChild = node.nextNode;
                else{
                    previous.nextNode = node.nextNode;
                }
                parent.childrenSize--;
            } else {
                RadixTreeNode<T> child = node.firstChild;
                node.key = node.key + child.key;
                node.real = child.real;
                node.value = child.value;
                node.firstChild = child.firstChild;
                node.childrenSize = child.childrenSize;
            }
        } else {
            node.value = null;
            node.real = false;
        }
        size--;
        return node;
    }
    /**
     * Check if the tree contains any entry corresponding to the given key.
     *
     * @param key The key that need to be searched in the tree.
     * @return return true if the key is present in the tree otherwise false
     */
    public boolean contains(String key){
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        return result.exactMatch && result.lastTry.real;
    }
    public T findPrefixGetValue(String key){
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        if (result.matchList.size() > 0)
            return result.matchList.getFirst().value;
        return null;
    }

    public RadixEntry<T> findPrefix(String key){
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        if (result.matchList.size() > 0)
            return new RadixEntry<T>(result.matchFullKeyList.getFirst(), result.matchList.getFirst().value);
        return null;
    }
    /**
     * Search for all prefixes of key.
     *
     * @param key The key for which prefixes need to be found.
     * @return The list of values those key start with the given prefix
     */
    public LinkedList<RadixEntry<T>> findPrefixes(String key){
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        if (result.matchList.size() == 0)
            return null;
        RadixTreeNode<T> nn ;
        ListIterator keyIt = result.matchFullKeyList.listIterator();
        LinkedList<RadixEntry<T>> matchList = new LinkedList<RadixEntry<T>>();
        while (keyIt.hasNext()) {
            nn = result.matchList.removeFirst();
            if (nn != null)
                matchList.addFirst(new RadixEntry<T>((String)keyIt.next(), nn.value));
        }
        return matchList;
    }

    public LinkedList<RadixEntry<T>> findPostfixes(String key, long limit){
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        if (result.partialMatch != null && result.lastTryNumMatchingChars + result.lastMatchKey.length() == key.length()) {
            return getChildren(result.partialMatch, key, limit);
        } else if (result.exactMatch) {
            return getChildren(result.lastTry, key, limit);
        }
        return new LinkedList<RadixEntry<T>>();
    }

    public LinkedList<String> findPostfixesGetkeys(String key, long limit){
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        if (result.partialMatch != null && result.lastTryNumMatchingChars + result.lastMatchKey.length() == key.length()) {
            return getChildrenKeys(result.partialMatch, key, limit);
        } else if (result.exactMatch) {
            return getChildrenKeys(result.lastTry, key, limit);
        }
        return new LinkedList<String>();
    }

    public LinkedList<T> findPostfixesGetValues(String key, long limit){
        RadixTreeSearchResult<T> result = searchKey(key, root, 2);
        if (result.partialMatch != null && result.lastTryNumMatchingChars + result.lastMatchKey.length() == key.length()) {
            return getChildrenValues(result.partialMatch, limit);
        } else if (result.exactMatch) {
            return getChildrenValues(result.lastTry, limit);
        }
        return new LinkedList<T>();
    }

    protected LinkedList<String> getChildrenKeys(RadixTreeNode<T> node, String levelKey, long limit) {
        if (limit < 0)
            limit = this.size;
        LinkedList<String> keys = new LinkedList<String>();
        Queue<String> levelKeys = new LinkedList<String>();
        Queue<RadixTreeNode<T>> iteratorQueue = new LinkedList<RadixTreeNode<T>>();
        if (node.real){
            keys.add(levelKey + node.key);
        }
        if (node.childrenSize > 0){
            levelKeys.add(levelKey);
            levelKey += node.key;
            node = node.firstChild;
        }
        while (keys.size() < limit) {
            if (node.real){
                keys.add(levelKey + node.key);
            }
            if (node.childrenSize > 0){
                levelKeys.add(levelKey);
                levelKey += node.key;
                if (node.nextNode != null)
                    iteratorQueue.add(node);
                node = node.firstChild;
                continue;
            }
            if (node.nextNode == null){
                while (!iteratorQueue.isEmpty() && node.nextNode == null){
                    node = iteratorQueue.remove();
                    levelKey = levelKeys.remove();
                }
                if (node.nextNode == null)
                    break;
            }
            node = node.nextNode;
        }
        return keys;
    }

    protected LinkedList<T> getChildrenValues(RadixTreeNode<T> node, long limit) {
        if (limit < 0)
            limit = this.size;
        LinkedList<T> values = new LinkedList<T>();
        if (node.real)
            values.add(node.value);
        Queue<RadixTreeNode<T>> queue = new LinkedList<RadixTreeNode<T>>();
        if (node.childrenSize > 0){
            queue.add(node);
            node = node.firstChild;
        } else
            return values;
        while (values.size() < limit) {
            if (node.real)
                values.add(node.value);
            if (node.childrenSize > 0){
                queue.add(node);
                node = node.firstChild;
                continue;
            }
            if (node.nextNode == null){
                while (!queue.isEmpty() && node.nextNode == null){
                    node = queue.remove();
                }
                if (node.nextNode == null)
                    break;
            }
            node = node.nextNode;
        }
        return values;
    }

    protected LinkedList<RadixEntry<T>> getChildren(RadixTreeNode<T> node, String levelKey, long limit) {
        if (limit < 0)
            limit = this.size;
        LinkedList<RadixEntry<T>> entries = new LinkedList<RadixEntry<T>>();
        Queue<String> levelKeys = new LinkedList<String>();
        Queue<RadixTreeNode<T>> iteratorQueue = new LinkedList<RadixTreeNode<T>>();
        if (node.real){
            entries.addFirst(new RadixEntry<T>(levelKey + node.key, node.value));
        }
        if (node.childrenSize > 0){
            levelKeys.add(levelKey);
            levelKey += node.key;
            node = node.firstChild;
        }
        while (entries.size() < limit) {
            if (node.real){
                entries.addFirst(new RadixEntry<T>(levelKey + node.key, node.value));
            }
            if (node.childrenSize > 0){
                levelKeys.add(levelKey);
                levelKey += node.key;
                if (node.nextNode != null)
                    iteratorQueue.add(node);
                node = node.firstChild;
                continue;
            }
            if (node.nextNode == null){
                while (!iteratorQueue.isEmpty() && node.nextNode == null){
                    node = iteratorQueue.remove();
                    levelKey = levelKeys.remove();
                }
                if (node.nextNode == null)
                    break;
            }
            node = node.nextNode;
        }
        return entries;
    }

    @Deprecated
    public void display(){
        display(root, "", 0);
    }

    @Deprecated
    protected void display(RadixTreeNode<T> node, String levelKey, long limit) {
        if (limit <= 0) limit = this.size;
        LinkedList<RadixTreeNode<T>> levelQueue = new LinkedList<RadixTreeNode<T>>();
        int level = 0;
        if (node.key.equals("")){
            if (node.childrenSize == 0){
                return;
            }
            node = node.firstChild;
        }
        long realCount = 0;
        String format = "";
        while (realCount < limit) {
            if (node.real){
                realCount++;
                System.out.printf("%s(%s)'%s' [%s]  key [%s]\n", format + "|-", levelKey, node.key, node.value.toString(), node.searchKey);
            } else
                System.out.printf("%s(%s)'%s'  key [%s]\n", format + "|-", levelKey, node.key, node.searchKey);
            if (node.childrenSize > 0){
                levelKey += node.key;
                levelQueue.add(node);
                node = node.firstChild;
                format += "| ";
                continue;
            }
            if (node.nextNode == null){

                while (!levelQueue.isEmpty() && node.nextNode == null){
                    node = levelQueue.removeLast();
                    levelKey = levelKey.substring(0, levelKey.length() - node.key.length());
                    format = format.substring(0, format.length() - 2);
                }
                if (node.nextNode == null)
                    break;
            }
            node = node.nextNode;
        }
    }

    /**
     * Return the number of real nodes in the Radix tree
     * @return the size of the tree
     */
    public long getSize() {
        return size;
    }
}
