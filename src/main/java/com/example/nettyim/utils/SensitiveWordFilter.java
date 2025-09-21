package com.example.nettyim.utils;

import java.util.*;

/**
 * 敏感词过滤工具类
 * 使用AC自动机算法进行高效的敏感词过滤
 */
public class SensitiveWordFilter {
    
    /**
     * AC自动机节点
     */
    private static class AcNode {
        Map<Character, AcNode> children = new HashMap<>();
        AcNode failure; // 失败指针
        boolean isEnd = false; // 是否为敏感词结尾
        String word; // 敏感词内容
        Integer action; // 处理方式：1-替换，2-拒绝，3-人工审核
        String replacement; // 替换词
        Integer level; // 敏感等级
        Integer category; // 分类
    }
    
    private AcNode root;
    
    public SensitiveWordFilter() {
        this.root = new AcNode();
    }
    
    /**
     * 构建AC自动机
     */
    public void buildAcAutomaton(List<SensitiveWordInfo> sensitiveWords) {
        // 构建Trie树
        for (SensitiveWordInfo wordInfo : sensitiveWords) {
            insert(wordInfo);
        }
        
        // 构建失败指针
        buildFailurePointer();
    }
    
    /**
     * 插入敏感词到Trie树
     */
    private void insert(SensitiveWordInfo wordInfo) {
        AcNode node = root;
        for (char c : wordInfo.getWord().toCharArray()) {
            node.children.computeIfAbsent(c, k -> new AcNode());
            node = node.children.get(c);
        }
        node.isEnd = true;
        node.word = wordInfo.getWord();
        node.action = wordInfo.getAction();
        node.replacement = wordInfo.getReplacement();
        node.level = wordInfo.getLevel();
        node.category = wordInfo.getCategory();
    }
    
    /**
     * 构建失败指针
     */
    private void buildFailurePointer() {
        Queue<AcNode> queue = new LinkedList<>();
        
        // 第一层节点的失败指针指向root
        for (AcNode child : root.children.values()) {
            child.failure = root;
            queue.offer(child);
        }
        
        while (!queue.isEmpty()) {
            AcNode current = queue.poll();
            
            for (Map.Entry<Character, AcNode> entry : current.children.entrySet()) {
                char c = entry.getKey();
                AcNode child = entry.getValue();
                
                // 寻找失败指针
                AcNode failure = current.failure;
                while (failure != null && !failure.children.containsKey(c)) {
                    failure = failure.failure;
                }
                
                if (failure == null) {
                    child.failure = root;
                } else {
                    child.failure = failure.children.get(c);
                }
                
                queue.offer(child);
            }
        }
    }
    
    /**
     * 检测敏感词
     */
    public SensitiveWordResult checkSensitiveWords(String text) {
        List<SensitiveWordMatch> matches = new ArrayList<>();
        AcNode node = root;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            // 跳转到合适的节点
            while (node != null && !node.children.containsKey(c)) {
                node = node.failure;
            }
            
            if (node == null) {
                node = root;
                continue;
            }
            
            node = node.children.get(c);
            
            // 检查当前节点及其失败指针路径上的所有匹配
            AcNode temp = node;
            while (temp != null) {
                if (temp.isEnd) {
                    int startIndex = i - temp.word.length() + 1;
                    SensitiveWordMatch match = new SensitiveWordMatch();
                    match.setWord(temp.word);
                    match.setStartIndex(startIndex);
                    match.setEndIndex(i);
                    match.setAction(temp.action);
                    match.setReplacement(temp.replacement);
                    match.setLevel(temp.level);
                    match.setCategory(temp.category);
                    matches.add(match);
                }
                temp = temp.failure;
            }
        }
        
        return new SensitiveWordResult(matches);
    }
    
    /**
     * 过滤敏感词
     */
    public String filterSensitiveWords(String text) {
        SensitiveWordResult result = checkSensitiveWords(text);
        if (result.getMatches().isEmpty()) {
            return text;
        }
        
        StringBuilder sb = new StringBuilder(text);
        
        // 按照位置从后往前替换，避免位置偏移
        result.getMatches().sort((a, b) -> b.getStartIndex() - a.getStartIndex());
        
        for (SensitiveWordMatch match : result.getMatches()) {
            if (match.getAction() == 1) { // 替换
                String replacement = match.getReplacement();
                if (replacement == null || replacement.isEmpty()) {
                    replacement = "*".repeat(match.getWord().length());
                }
                sb.replace(match.getStartIndex(), match.getEndIndex() + 1, replacement);
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 敏感词信息
     */
    public static class SensitiveWordInfo {
        private String word;
        private Integer category;
        private Integer level;
        private Integer action;
        private String replacement;
        
        public SensitiveWordInfo(String word, Integer category, Integer level, Integer action, String replacement) {
            this.word = word;
            this.category = category;
            this.level = level;
            this.action = action;
            this.replacement = replacement;
        }
        
        // Getters
        public String getWord() { return word; }
        public Integer getCategory() { return category; }
        public Integer getLevel() { return level; }
        public Integer getAction() { return action; }
        public String getReplacement() { return replacement; }
    }
    
    /**
     * 敏感词匹配结果
     */
    public static class SensitiveWordMatch {
        private String word;
        private int startIndex;
        private int endIndex;
        private Integer action;
        private String replacement;
        private Integer level;
        private Integer category;
        
        // Getters and Setters
        public String getWord() { return word; }
        public void setWord(String word) { this.word = word; }
        public int getStartIndex() { return startIndex; }
        public void setStartIndex(int startIndex) { this.startIndex = startIndex; }
        public int getEndIndex() { return endIndex; }
        public void setEndIndex(int endIndex) { this.endIndex = endIndex; }
        public Integer getAction() { return action; }
        public void setAction(Integer action) { this.action = action; }
        public String getReplacement() { return replacement; }
        public void setReplacement(String replacement) { this.replacement = replacement; }
        public Integer getLevel() { return level; }
        public void setLevel(Integer level) { this.level = level; }
        public Integer getCategory() { return category; }
        public void setCategory(Integer category) { this.category = category; }
    }
    
    /**
     * 敏感词检测结果
     */
    public static class SensitiveWordResult {
        private List<SensitiveWordMatch> matches;
        
        public SensitiveWordResult(List<SensitiveWordMatch> matches) {
            this.matches = matches;
        }
        
        public List<SensitiveWordMatch> getMatches() {
            return matches;
        }
        
        public boolean hasSensitiveWords() {
            return !matches.isEmpty();
        }
        
        public boolean hasHighLevelWords() {
            return matches.stream().anyMatch(match -> match.getLevel() >= 3);
        }
        
        public boolean needsManualReview() {
            return matches.stream().anyMatch(match -> match.getAction() == 3);
        }
        
        public boolean shouldReject() {
            return matches.stream().anyMatch(match -> match.getAction() == 2);
        }
        
        public List<String> getSensitiveWords() {
            return matches.stream().map(SensitiveWordMatch::getWord).distinct().toList();
        }
    }
}