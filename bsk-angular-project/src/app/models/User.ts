export interface User {
  id?: number;
  username: string;
  password: string;
  email?: string;
  enabled?: boolean;
  authorities?: Authority[];
}

export interface Authority {
  id?: number;
  authority: string;
}
