-- Create player table if not exists
CREATE TABLE IF NOT EXISTS player (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    player_wins_counter INTEGER DEFAULT 0 NOT NULL
);

-- Create index on name for faster lookups
CREATE INDEX IF NOT EXISTS idx_player_name ON player(name);

-- Create index on wins counter for ranking queries
CREATE INDEX IF NOT EXISTS idx_player_wins ON player(player_wins_counter DESC);
