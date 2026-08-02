package club.saltfish.homeservice.llm

/**
 * LLM 文本生成客户端。用于根据上下文生成自然语言播报。
 */
interface LlmClient {
    /**
     * 生成回复文本。
     * @param systemPrompt 约束风格/角色的系统提示
     * @param userPrompt 提供具体上下文的用户提示
     * @return 成功返回生成的文本
     */
    suspend fun chat(systemPrompt: String, userPrompt: String): Result<String>
}
